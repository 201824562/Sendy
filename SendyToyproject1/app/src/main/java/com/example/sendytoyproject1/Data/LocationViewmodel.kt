package com.example.sendytoyproject1.Data

import android.app.Application
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

    private val _alllocations = MutableLiveData<List<LocationItemData>>()
            val alllocations : LiveData<List<LocationItemData>>
                get() = _alllocations

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

    fun getItems() {
        //Livedata.value가 안됨. -> 밑 코드에서 Livedata를 벗겨내야함. 그 후 map (Entity -> LocationItemData로 타입 변경)
        Log.d("ViewmodelChecking", "들어왔나")
        val test = repository.getItems()
        Log.d("ViewmodelChecking", "$test")

        val locationItemDataLiveData = Transformations.map(test){
            val items: MutableList<LocationItemData> = mutableListOf()
            Log.d("ViewmodelChecking", "$it")
            for (item in it){
                val dateTime = item.datetime
                val address = item.address
                val clickedValue = false

                items.add(LocationItemData(dateTime,address ?: "" ,clickedValue))
            }// list 'items'가 생성됨.
            Log.d("ViewmodelChecking", "$items")
            _alllocations.value = items
            Log.d("ViewmodelChecking", "$alllocations")
        }
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



    fun insert(it: LocationEntity)  { //이것도 itemData로 받아온 후 Entity형식으로 바꿔야 함.
        viewModelScope.launch(Dispatchers.IO){      //이거 맞나아.
            repository.insert(it)
        }
    }

    /*
    fun delete(it: SparseBooleanArray)= viewModelScope.launch(Dispatchers.IO){
        repository.delete(it)
    }
    */


}