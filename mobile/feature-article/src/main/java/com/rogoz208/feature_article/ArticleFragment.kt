package com.rogoz208.feature_article

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.rogoz208.feature_article.databinding.FragmentArticleBinding
import com.rogoz208.feature_article.di.DaggerFeatureArticleComponent
import com.rogoz208.feature_article.di.FeatureArticleComponent
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.di.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class ArticleFragment : Fragment(R.layout.fragment_article),
    IHasComponent<FeatureArticleComponent>, MenuProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<ArticleViewModel> { viewModelFactory }

    private val binding by viewBinding(FragmentArticleBinding::bind)

    private var menu: Menu? = null

    private var savedIcon = mapOf(
        true to com.rogoz208.mobile_common.R.drawable.ic_save_filled_24dp,
        false to com.rogoz208.mobile_common.R.drawable.ic_save_24dp
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transparentStatusBar(true)
        getArticleFromArguments()
        initToolbar()
        initViewModel()
    }

    override fun onDestroyView() {
        transparentStatusBar(false)
        super.onDestroyView()
    }

    override fun getComponent(): FeatureArticleComponent {
        return DaggerFeatureArticleComponent.builder()
            .featureArticleDependencies(XInjectionManager.findComponent()).build()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        this.menu = menu
        menuInflater.inflate(R.menu.article_toolbar_menu, menu)
        viewModel.onMenuInflated()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.toolbar_menu_item_save -> {
                viewModel.onArticleSaveClick()
                return true
            }
        }
        return false
    }

    private fun transparentStatusBar(transparent: Boolean) {
        val activity = requireActivity() as AppCompatActivity
        if (transparent) {
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.statusBarColor = resources.getColor(
                com.rogoz208.mobile_common.R.color.light_primary_60, activity.theme
            )
        }
    }

    private fun getArticleFromArguments() {
        arguments?.apply {
            val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARTICLE_EXTRA_KEY, ArticleUI::class.java)
            } else {
                getParcelable(ARTICLE_EXTRA_KEY)
            }
            article?.let { viewModel.onGettingArticle(it) }
        }
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }
            addMenuProvider(this@ArticleFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleStateFlow.collectLatest { article ->
                    with(binding) {
                        Glide.with(this@ArticleFragment).load(article.urlToImage)
                            .into(toolbarImageView)

                        setToolbarTitle(article.title)
                        articleDescriptionTextView.text = article.description
                        articleDateTextView.text = formatDate(article.publishedAt)
                        articleSourceNameTextView.text = article.sourceName
                        setClickableUrlInTextView(
                            article.content, article.url, articleContentTextView
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleSavedStateFlow.collectLatest { isSaved ->
                    menu?.findItem(R.id.toolbar_menu_item_save)?.icon = ContextCompat.getDrawable(
                        requireContext(), savedIcon[isSaved]!!
                    )
                }
            }
        }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm aaa", Locale.US)
        return dateFormat.format(date)
    }

    private fun setClickableUrlInTextView(text: String?, url: String, textView: TextView) {
        val spannableString = SpannableString(text ?: url)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(tv: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val start = spannableString.lastIndexOf(". ") + 2
        val end = spannableString.length - 1
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setToolbarTitle(title: String) {
        var isShow = true
        var scrollRange = -1
        with(binding) {
            appbar.addOnOffsetChangedListener { barLayout, verticalOffset ->
                when {
                    scrollRange == -1 -> {
                        scrollRange = barLayout?.totalScrollRange!!
                    }

                    scrollRange + verticalOffset == 0 -> {
                        collapsingToolbar.title = title
                        isShow = true
                    }

                    isShow -> {
                        collapsingToolbar.title = " "
                        isShow = false
                    }
                }
            }
        }
    }

    companion object {
        const val ARTICLE_EXTRA_KEY = "ARTICLE_EXTRA_KEY"

        fun newInstance(article: ArticleUI): ArticleFragment = ArticleFragment().apply {
            arguments = bundleOf(ARTICLE_EXTRA_KEY to article)
        }
    }
}

fun createArticleScreen(article: ArticleUI): Fragment = ArticleFragment.newInstance(article)