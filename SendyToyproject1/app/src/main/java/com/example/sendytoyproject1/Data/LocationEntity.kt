package com.example.sendytoyproject1.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @PrimaryKey
    @ColumnInfo(name="datetime")
    val datetime: String,

    @ColumnInfo(name="location")
    val location: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Int
)
