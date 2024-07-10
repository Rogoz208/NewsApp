package com.rogoz208.database_api.model

import androidx.room.ColumnInfo

data class SourceDBO(
    @ColumnInfo("id") val id: String?,
    @ColumnInfo("name") val name: String?
)