package com.example.sendytoyproject1.Data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//다오 -> 서버의 데이터를 관리하는 로직들.(리포지토리에서 서버DB에 접근을 요청할 때 수행할 함수) //흐름이 일방적이다.(구독자 vs 유투버)
//이번 어플에서는 서버 안써서 필요없음.

@Dao
interface LocationDao {

    //시간별 정렬 제대로 되는지 확인 필요
    @Query("SELECT * from location_table ORDER BY datetime ASC")
    fun getAlphabetizedLocations(): LiveData<List<LocationEntity>> //Livedata로 감싸주어 데이터 변화의 감지가 필요하다.

    @Insert(onConflict = OnConflictStrategy.IGNORE) //동일한 데이터가 들어오면 받지 않는다. (근데 시간때문에 그럴일 없긴함.)
    fun insert(locationEntity: LocationEntity)

    /*@Query("DELETE FROM user_table")
    suspend fun deleteAll()*/
}