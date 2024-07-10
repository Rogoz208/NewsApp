package com.rogoz208.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rogoz208.database.NewsDatabase.Companion.DATABASE_NAME
import com.rogoz208.database.dao.ArticleDao
import com.rogoz208.database_api.model.ArticleDBO
import com.rogoz208.database.utils.Converters

@Database(entities = [ArticleDBO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticleDao

    companion object {
        const val DATABASE_NAME = "news"
    }
}

internal fun NewsDatabase(context: Context): NewsDatabase = Room.databaseBuilder(
    checkNotNull(context.applicationContext),
    NewsDatabase::class.java,
    DATABASE_NAME
).build()