package com.example.sendytoyproject1.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @ColumnInfo(name="datetime")
    val datetime: String,

    @ColumnInfo(name="address")
    val address: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)
