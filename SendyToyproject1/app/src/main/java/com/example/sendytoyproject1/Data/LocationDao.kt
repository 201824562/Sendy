package com.example.sendytoyproject1.Data

import androidx.lifecycle.LiveData
import androidx.room.*

//다오 -> 데이터베이스 접근 메소드들 (쿼리들) _직접 작성함.

@Dao //DTO
interface LocationDao {

    //시간별 정렬 제대로 되는지 확인 필요
    @Query("SELECT * from location_table ORDER BY id ASC")
    fun getAlphabetizedLocations(): LiveData<List<LocationEntity>> //Livedata로 감싸주어 데이터 변화의 감지가 필요하다.

    @Insert(onConflict = OnConflictStrategy.REPLACE) //동일한 데이터가 들어오면 받지 않는다. (근데 시간때문에 그럴일 없긴함.)
    fun insert(locationEntity: LocationEntity)

    @Query("UPDATE location_table SET memo=:memo WHERE id=:id")
    fun insertmemo(id:Int, memo: String?)

    @Query("DELETE from location_table WHERE id = :it")
    fun delete(it: Int)

}