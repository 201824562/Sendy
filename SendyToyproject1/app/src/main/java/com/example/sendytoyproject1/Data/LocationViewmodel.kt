package com.example.sendytoyproject1.Data

import android.app.Application
import android.location.Location
import android.util.Log
import android.util.SparseBooleanArray
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.example.sendytoyproject1.LocationItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//리포지토리의 데이터들을 관리하는 로직.

class LocationViewmodel(application: Application) : AndroidViewModel(application) {


    //서버가 있다면 필요하나, 없어서 리포지토리 사용 X
    private val repository = LocationRepository(AppDatabase.getDatabase(application, viewModelScope))
    /*
    private val _alllocations = MutableLiveData<List<LocationItemData>>()
            val alllocations : LiveData<List<LocationItemData>>
                get() = _alllocations*/


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


 companion object {
        var SelectedItemslist = SparseBooleanArray()
    }

    fun getItems(): LiveData<List<LocationItemData>> {
        val allitems = repository.getItems() //가져온 친구는  LiveData<List<LocationItemData>>
        return allitems
    }


//        repository.getItems()
//            val allLocations  = repository.getItems() //가져온 데이터가 Livedata<List<Entity>> 형식이다.
//            //_alllocations.value = allLocations
//
//            allLocations.observe(){
//                val items: MutableList<LocationItemData> = mutableListOf()
//
//                for (item in it){
//                    val datetime : String = item.datetime
//                    val address  : String? = item.address
//                    var clickedvalue : Boolean = false          //이걸 map으로 바꿔주기.
//
//
//                    address?.let { it1 -> LocationItemData(datetime, it1, clickedvalue) }?.let { it2 ->
//                        items.add(
//                            it2
//                        )
//                    }
//
//
//                }// 타입 변환된 items가 나옴.(리스트)
//                _alllocations.value = items
//            }


    /*
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

    */



    fun insert(it: LocationItemData)  = viewModelScope.launch(Dispatchers.IO)  { //이것도 itemData로 받아온 후 Entity형식으로 바꿔야 함.
        repository.insert(it)
    }

    fun delete() = viewModelScope.launch(Dispatchers.IO){
        Log.d("LocationViewmodel", "$SelectedItemslist")    //여기서 true인 애만 어뜨케 삭제하지.
        for (i in (SelectedItemslist.size()-1) downTo 0){
            var key = SelectedItemslist.keyAt(i) //i는 리스트자체의 인덱스, key는 실제 데이터의 인덱스
            //Log.d("LocationViewmodel", "모든 i + $key")
            repository.delete(key)

        }
    }


    fun pushSparseArray(it : SparseBooleanArray) = viewModelScope.launch(Dispatchers.IO){
        //여기서 받아온 it으로 true인 애 찾아서 delete 해줘야함.
        SelectedItemslist  = it
    }


}