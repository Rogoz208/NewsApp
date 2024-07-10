package com.rogoz208.database_api.di

import com.rogoz208.database_api.NewsDataBaseInteractor

interface NewsDatabaseProvider {
    val newsDatabaseInteractor: NewsDataBaseInteractor
}