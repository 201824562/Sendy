package com.example.sendytoyproject1.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity == Persist한 정보 (앱이 종료되어도 남아있는 정보)
@Entity(tableName = "location_table")
data class LocationEntity(
    @ColumnInfo(name="datetime")
    val datetime: String,

    @ColumnInfo(name="address")
    val address: String?,


    //클릭여부는 뷰의 데이터이지 앱의 데이터가 X/ 따라서 없는 게 맞다.
    //@ColumnInfo(name="clickedValue")
    //var clickedValue: Boolean = false,

    @PrimaryKey(autoGenerate = true) //삭제하면 어케되는가..?
    val id: Int = 0

)
