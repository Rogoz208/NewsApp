package com.rogoz208.newsdata.di

import com.rogoz208.newsdata_api.di.NewsDataDependencies
import com.rogoz208.newsdata_api.di.NewsDataProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsDataModule::class], dependencies = [NewsDataDependencies::class])
interface NewsDataComponent : NewsDataProvider