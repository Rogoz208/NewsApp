package com.rogoz208.database_api.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "articles")
data class ArticleDBO(
    @PrimaryKey val id: String,
    @Embedded(prefix = "source_") val sourceDBO: SourceDBO,
    @ColumnInfo("author") val author: String?,
    @ColumnInfo("title") val title: String?,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("url") val url: String,
    @ColumnInfo("urlToImage") val urlToImage: String?,
    @ColumnInfo("publishedAt") val publishedAt: Date,
    @ColumnInfo("content") val content: String?,
    @ColumnInfo("category") val category: String?,
    @ColumnInfo("saved") val saved: Boolean = false
)

