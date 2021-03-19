package com.example.sendytoyproject1.Data

import android.location.Location
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sendytoyproject1.LocationItemData

//리포지토리-> 앱실행중 저장 공간 & 리포지토리의 데이터 관리 로직함수(ViewModel에서 리포지DB에 접근을 요청할 때 수행할 함수) //흐름이 일방적이다.(구독자 vs 유투버)

class LocationRepository(locationdatabase: AppDatabase) {

    //다오의 데이터베이스 가져옴. (여기다가 리포지 값을 넣어줘야 하므로)
    //다오 == 앱 단위/ 리포지 == UI간 단위
    private val LocationDao = locationdatabase.LocationDao()

    private val _alllocations = MutableLiveData<List<LocationItemData>>()
    val alllocations : LiveData<List<LocationItemData>>
        get() = _alllocations

    /*
    private val locationlist = mutableListOf<LocationEntity>()  //View와 ViewModel의 관계에서  리스트 데이터를 LiveData로 관리하며 내부 아이템의 변경사항까지 Observing 하도록 하기 위한 용도.
    private val _AllLocations = MutableLiveData<List<LocationEntity>>() //setvalue(), postvalue() == value
    val AllLocations : LiveData<List<LocationEntity>> = _AllLocations*/


    fun getItems() : LiveData<List<LocationItemData>> {
        val location= LocationDao.getAlphabetizedLocations()

        // 밑 코드에서 Livedata를 벗겨내야함. 그 후 map (Entity -> LocationItemData로 타입 변경)
        val locationItemDataLiveData = Transformations.map(location){ it -> it.map {
            mappingEntitytoItemdata(it)
        }
            //_alllocations.value = locationItemDataLiveData
            // Log.d("ViewmodelChecking", "$alllocations")
        }
        return locationItemDataLiveData
    }

    private fun mappingEntitytoItemdata(it: LocationEntity) : LocationItemData {
        val dateTime = it.datetime
        val address  = it.address.toString()
        val memo = it.memo.toString()
        val id = it.id

        return (LocationItemData(dateTime, address, memo, id ,false))
    }


    fun insert(it: LocationItemData) {
        LocationDao.insert(LocationEntity(it.datetime, it.address, it.memo))
        //Log.d(ContentValues.TAG, "들어간 값" + AllLocations.value.toString())
    }

    fun insertmemo(id:Int, memo: String?) {
        LocationDao.insertmemo(id, memo)
        //Log.d(ContentValues.TAG, "들어간 값" + AllLocations.value.toString())
    }

    fun delete(it: Int){
        LocationDao.delete(it)
    }


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
}