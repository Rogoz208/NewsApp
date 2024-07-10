package com.rogoz208.feature_saved

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rogoz208.feature_saved.databinding.FragmentSavedBinding
import com.rogoz208.feature_saved.di.DaggerFeatureSavedComponent
import com.rogoz208.feature_saved.di.FeatureSavedComponent
import com.rogoz208.feature_saved.recycler.SavedArticlesAdapter
import com.rogoz208.feature_saved.recycler.OnSavedArticleItemClickListener
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.di.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

internal class SavedFragment : Fragment(R.layout.fragment_saved),
    IHasComponent<FeatureSavedComponent>, MenuProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<SavedArticlesViewModel> { viewModelFactory }

    private val binding by viewBinding(FragmentSavedBinding::bind)

    private val adapter = SavedArticlesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()
        initViewModel()
        viewModel.onScreenLoaded()
    }

    override fun getComponent(): FeatureSavedComponent {
        return DaggerFeatureSavedComponent.builder()
            .featureSavedDependencies(XInjectionManager.findComponent()).build()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.saved_toolbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val menuItemId = menuItem.itemId
        if (menuItemId == R.id.toolbar_menu_item_search) {
            binding.searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    Toast.makeText(requireContext(), s.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun afterTextChanged(s: Editable) {}
            })
            binding.searchView.show()
        }

        if (menuItemId == R.id.toolbar_menu_item_filter) {
            viewModel.onFiltersMenuItemClicked()
            return true
        }
        return false
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            addMenuProvider(this@SavedFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), linearLayoutManager.orientation)

        adapter.setOnItemClickListener(object : OnSavedArticleItemClickListener {
            override fun onArticleClick(item: ArticleUI, position: Int) {
                viewModel.onArticleItemClicked(item, position)
            }

            override fun onArticleLongClick(item: ArticleUI, itemView: View, position: Int) {
                TODO("Not yet implemented")
            }
        })

        with(binding) {
            recyclerView.setLayoutManager(linearLayoutManager)
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.setAdapter(adapter)
            swipeContainer.setOnRefreshListener { viewModel.onScreenLoaded() }
        }
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedArticlesStateFlow.collectLatest { articles ->
                    adapter.setNewData(articles)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isProgressStateFlow.collectLatest { isProgress ->
                    with(binding) {
                        swipeContainer.isRefreshing = isProgress
                        progressBar.visibility = if (isProgress) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): SavedFragment = SavedFragment()
    }
}

fun createSavedScreen(): Fragment = SavedFragment.newInstance()