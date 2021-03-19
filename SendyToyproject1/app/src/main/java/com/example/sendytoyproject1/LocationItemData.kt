package com.example.sendytoyproject1

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationItemData (  //xml에 binding으로 뷰와 연결해줌.
    val datetime: String,
    val address: String,
    val memo: String,
    val id: Int,
    var clickedValue : Boolean = false
        ) : Parcelable