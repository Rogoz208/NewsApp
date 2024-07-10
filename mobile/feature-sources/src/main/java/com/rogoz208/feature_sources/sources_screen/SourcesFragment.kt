package com.rogoz208.feature_sources.sources_screen

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
import com.rogoz208.feature_sources.R
import com.rogoz208.feature_sources.databinding.FragmentSourcesBinding
import com.rogoz208.feature_sources.di.DaggerFeatureSourcesComponent
import com.rogoz208.feature_sources.di.FeatureSourcesComponent
import com.rogoz208.feature_sources.sources_screen.recycler.OnSourceItemClickListener
import com.rogoz208.feature_sources.sources_screen.recycler.SourceAdapter
import com.rogoz208.mobile_common.model.SourceUI
import com.rogoz208.mobile_common.di.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import javax.inject.Inject

internal class SourcesFragment : Fragment(R.layout.fragment_sources),
    IHasComponent<FeatureSourcesComponent>, MenuProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<SourcesViewModel> { viewModelFactory }

    private val binding by viewBinding(FragmentSourcesBinding::bind)

    private val adapter = SourceAdapter()

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

    override fun getComponent(): FeatureSourcesComponent {
        return DaggerFeatureSourcesComponent.builder()
            .featureSourcesDependencies(XInjectionManager.findComponent()).build()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.sources_toolbar_menu, menu)
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
            addMenuProvider(this@SourcesFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), linearLayoutManager.orientation)

        adapter.setOnItemClickListener(object : OnSourceItemClickListener {
            override fun onSourceClickListener(item: SourceUI, position: Int) {
                viewModel.onSourceItemClicked(item, position)
            }

            override fun onSourceLongClickListener(item: SourceUI, itemView: View, position: Int) {
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
                viewModel.sourcesStateFlow.collectLatest { sources ->
                    adapter.setNewData(sources)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isProgressStateFlow.collectLatest { isProgress ->
                    binding.swipeContainer.isRefreshing = isProgress
                }
            }
        }
    }

    companion object {
        fun newInstance(): SourcesFragment {
            return SourcesFragment()
        }
    }
}

fun createSourcesScreen(): Fragment = SourcesFragment.newInstance()