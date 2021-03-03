package com.example.sendytoyproject1.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    //시간별 정렬 제대로 되는지 확인 필요
    @Query("SELECT * from location_table ORDER BY datetime ASC")
    fun getAlphabetizedUsers(): LiveData<List<LocationEntity>> //Livedata로 감싸주어 데이터 변화의 감지가 필요하다.

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(locationEntity: LocationEntity)  //동일한 데이터가 들어오면 받지 않는다. (근데 시간때문에 그럴일 없긴함.)

    /*@Query("DELETE FROM user_table")
    suspend fun deleteAll()*/
}