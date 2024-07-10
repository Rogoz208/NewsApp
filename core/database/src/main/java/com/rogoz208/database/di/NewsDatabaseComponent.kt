package com.rogoz208.database.di

import com.rogoz208.database_api.di.NewsDatabaseProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsDatabaseModule::class])
interface NewsDatabaseComponent : NewsDatabaseProvider {

    @Component.Builder
    interface Builder {
        fun setNewsDatabaseModule(module: NewsDatabaseModule): Builder

        fun build(): NewsDatabaseComponent
    }
}