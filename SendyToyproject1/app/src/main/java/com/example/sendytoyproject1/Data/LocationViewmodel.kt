package com.example.sendytoyproject1.Data

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.util.SparseBooleanArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sendytoyproject1.Location_Item_Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//리포지토리의 데이터들을 관리하는 로직.

class LocationViewmodel(application: Application) : AndroidViewModel(application) {

    //서버가 있다면 필요하나, 없어서 리포지토리 사용 X
    val repository = LocationRepository(AppDatabase.getDatabase(application, viewModelScope))
    var alllocations  = repository.allLocations

    companion object {
        var SelectedItemslist = SparseBooleanArray()
    }

    //ObservableField 변수 : Observe되는 변수의 값이 변하는지 동적 체크 -> Observe 해제 필요 O.
    //Livedata 변수 : 변수의 값이 변하는지 동적 체크 -> Observe 해제 필요 X.(자동해제해줌.)           -> 보통 얘를 더 많이 쓴다.

    //[1.Observable 방법]
    //var sendedDatetimeText: ObservableField<String> = ObservableField("DateTime")
    //var sendedAddressText: ObservableField<String> = ObservableField("Address")

    //[2.Livedata 방법]
    //private val teaDao = mDatabase.teaDao() //서버에서 가져온 데이터 연동할 때 사용함.



    //리포지토리의 데이터를 관리할 함수들
    /*fun getItems(): LiveData<List<LocationEntity>>? {
        return this.alllocations
    }*/

    fun getItems(locations: List<LocationEntity>): MutableList<Location_Item_Data> {
        var items: MutableList<Location_Item_Data> = mutableListOf()

        if (locations != null){
            for (location in locations){
                val datetime: String = location.datetime
                val address: String? = location.address

                address?.let {
                    Location_Item_Data(
                        datetime,
                        it
                    )
                }?.let {
                    items.add(
                        it
                    )
                }
            }
        }
        return items
    }

    fun insert(it: LocationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(it)
    }

    /*
    fun delete(it: SparseBooleanArray)= viewModelScope.launch(Dispatchers.IO){
        repository.delete(it)
    }
    */


}