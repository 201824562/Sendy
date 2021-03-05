package com.example.sendytoyproject1.Data

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//리포지토리-> 내부저장 공간 & 리포지토리의 데이터 관리 로직함수(ViewModel에서 리포지DB에 접근을 요청할 때 수행할 함수) //흐름이 일방적이다.(구독자 vs 유투버)


class LocationRepository(application: Application) {

    //(밑 변수들) 서버가 있으면 원래 룸에서 데이터 가져와서 초기화 해주어야 함.

    private val locationlist = mutableListOf<LocationEntity>()  //View와 ViewModel의 관계에서  리스트 데이터를 LiveData로 관리하며 내부 아이템의 변경사항까지 Observing 하도록 하기 위한 용도.
    private val _AllLocations = MutableLiveData<List<LocationEntity>>() //setvalue(), postvalue() == value
    val AllLocations : LiveData<List<LocationEntity>> = _AllLocations


    fun getAll(): LiveData<List<LocationEntity>>? {
        return AllLocations
    }


    fun insert(it: LocationEntity?) {       //만약 이미 있던 것일 경우,,,?
        if (it != null) {
            locationlist.add(it)        //View와 ViewModel의 관계에서  리스트 데이터를 LiveData로 관리하며 내부 아이템의 변경사항까지 Observing 하도록 하기 위한 용도.
        }
        _AllLocations.value = locationlist

        Log.d(ContentValues.TAG, "들어간 값" + AllLocations.value.toString())
    }

    //제거 메소드가 필요하다.
    //fun delete(it: )

}