package com.example.sendytoyproject1

data class LocationItemData (  //xml에 binding으로 뷰와 연결해줌.
    val datetime: String,
    val address: String,
    val id: Int,
    var clickedValue : Boolean = false
        )