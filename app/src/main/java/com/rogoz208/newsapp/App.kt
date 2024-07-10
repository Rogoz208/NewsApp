package com.rogoz208.newsapp

import android.app.Application
import com.rogoz208.database.di.DaggerNewsDatabaseComponent
import com.rogoz208.database.di.NewsDatabaseComponent
import com.rogoz208.database.di.NewsDatabaseModule
import com.rogoz208.navigation.di.DaggerNavigationComponent
import com.rogoz208.navigation.di.NavigationComponent
import com.rogoz208.newsapi.di.DaggerNewsApiComponent
import com.rogoz208.newsapi.di.NewsApiComponent
import com.rogoz208.newsapp.di.ApplicationComponent
import com.rogoz208.newsapp.di.ApplicationModule
import com.rogoz208.newsapp.di.DaggerApplicationComponent
import com.rogoz208.newsapp.di.aggregators.DaggerNewsDataDependenciesAggregator
import com.rogoz208.newsapp.di.aggregators.NewsDataDependenciesAggregator
import com.rogoz208.newsdata.di.DaggerNewsDataComponent
import com.rogoz208.newsdata.di.NewsDataComponent
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

class App : Application() {

    internal val applicationComponent: ApplicationComponent
        get() = component!!

    override fun onCreate() {
        super.onCreate()

        bindDaggerComponents()
    }

    private fun bindDaggerComponents() {
        XInjectionManager.init(this)

        XInjectionManager.bindComponent(object : IHasComponent<NavigationComponent> {
            override fun getComponent(): NavigationComponent =
                DaggerNavigationComponent.builder().build()
        })

        XInjectionManager.bindComponent(object : IHasComponent<NewsDatabaseComponent> {
            override fun getComponent(): NewsDatabaseComponent =
                DaggerNewsDatabaseComponent.builder()
                    .setNewsDatabaseModule(NewsDatabaseModule(this@App))
                    .build()

        })

        XInjectionManager.bindComponent(object : IHasComponent<NewsApiComponent> {
            override fun getComponent(): NewsApiComponent =
                DaggerNewsApiComponent.builder().build()
        })

        XInjectionManager.bindComponent(object : IHasComponent<NewsDataDependenciesAggregator> {
            override fun getComponent(): NewsDataDependenciesAggregator =
                DaggerNewsDataDependenciesAggregator.builder()
                    .newsDatabaseProvider(XInjectionManager.findComponent())
                    .newsApiProvider(XInjectionManager.findComponent())
                    .build()
        })

        XInjectionManager.bindComponent(object : IHasComponent<NewsDataComponent> {
            override fun getComponent(): NewsDataComponent = DaggerNewsDataComponent.builder()
                .newsDataDependencies(XInjectionManager.findComponent())
                .build()
        })

        component = XInjectionManager.bindComponent(object : IHasComponent<ApplicationComponent> {
            override fun getComponent(): ApplicationComponent =
                DaggerApplicationComponent.builder()
                    .setApplicationModule(ApplicationModule(this@App))
                    .setNavigationProvider(XInjectionManager.findComponent())
                    .setNewsDatabaseProvider(XInjectionManager.findComponent())
                    .setNewsApiProvider(XInjectionManager.findComponent())
                    .setNewsDataProvider(XInjectionManager.findComponent())
                    .build()
        })
    }

    companion object {
        @JvmStatic
        private var component: ApplicationComponent? = null
            get() = field ?: throw RuntimeException("Application component is null")
    }
}