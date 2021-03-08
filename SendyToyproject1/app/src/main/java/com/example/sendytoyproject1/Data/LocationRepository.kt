package com.example.sendytoyproject1.Data

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//리포지토리-> 내부저장 공간 & 리포지토리의 데이터 관리 로직함수(ViewModel에서 리포지DB에 접근을 요청할 때 수행할 함수) //흐름이 일방적이다.(구독자 vs 유투버)


class LocationRepository(locationdatabase: AppDatabase) {

    //다오의 데이터베이스 가져옴. (여기다가 리포지 값을 넣어줘야 하므로)
    //다오 == 앱 단위/ 리포지 == UI간 단위
    private val LocationDao = locationdatabase.LocationDao()
    val allLocations: LiveData<List<LocationEntity>> = LocationDao.getAlphabetizedLocations()
    /*
    private val locationlist = mutableListOf<LocationEntity>()  //View와 ViewModel의 관계에서  리스트 데이터를 LiveData로 관리하며 내부 아이템의 변경사항까지 Observing 하도록 하기 위한 용도.
    private val _AllLocations = MutableLiveData<List<LocationEntity>>() //setvalue(), postvalue() == value
    val AllLocations : LiveData<List<LocationEntity>> = _AllLocations*/

    companion object{
        private var sInstance: LocationRepository? = null
        fun getInstance(database: AppDatabase): LocationRepository {
            return sInstance
                    ?: synchronized(this){
                        val instance = LocationRepository(database)
                        sInstance = instance
                        instance
                    }
        }
    }


     fun insert(it: LocationEntity?) {       //만약 이미 있던 것일 경우,,,? (처리필요)
        if (it != null) {
            LocationDao.insert(it)
        }
        //Log.d(ContentValues.TAG, "들어간 값" + AllLocations.value.toString())
    }

    //제거 메소드가 필요하다.
    //fun delete(it: )

}