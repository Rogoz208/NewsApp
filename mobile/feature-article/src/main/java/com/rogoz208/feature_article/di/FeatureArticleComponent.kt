package com.rogoz208.feature_article.di

import com.rogoz208.feature_article.ArticleFragment
import com.rogoz208.feature_article_api.FeatureArticleDependencies
import com.rogoz208.mobile_common.di.FragmentScope
import dagger.Component

@FragmentScope
@Component(
    modules = [FeatureArticleModule::class],
    dependencies = [FeatureArticleDependencies::class]
)
internal interface FeatureArticleComponent {

    fun inject(fragment: ArticleFragment)
}