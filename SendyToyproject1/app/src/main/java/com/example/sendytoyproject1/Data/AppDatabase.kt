package com.example.sendytoyproject1.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// (룸1)'앱데이타베이스' ==SQLite 객체 -> DB 인터페이스를 생성


@Database(entities = [LocationEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() { //RoomDatabase 객체를 상속하고 리턴값으로 Dao를 가지는 추상클래스
    abstract fun LocationDao(): LocationDao     //이 객체를 생성하는데, 비용이 많이 들기 때문에 Singleton 패턴으로 1개만 생성되게 해야함.
                                                //singleton pattern :객체를 하나만 생성하고 이를 어디에서든지 참조할 수 있게 하는 패턴.

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(        //'getDatabase'메소드 구현 -> Viewmodel에서 appdatabase 객체에 접근할 때 사용할 객체관련 함수이다.
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {  // if the INSTANCE is not null, then return it,  if it is, then create the database
            return INSTANCE ?: synchronized(this) { //Dao를 리턴한다. 그 Dao는 synchroinized됨. 다음과 같이
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallback(scope)) //이미 만들어져 있는 경우 만들어져있는 애를 가져온다. 아니면 새로 빌드(위)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    private class AppDatabaseCallback(      //이전에 만들어진 정보가 있는 경우 (null이 아닌경우), 그 정보를 가져온다.
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) { //데이터베이스 공간에 데이터를 처음 넣어줌.
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    LocationDatabase(database.LocationDao())
                }
            }
        }

        //이번 어플에서는 초기데이터가 없기 때문에 필요가 없다.
        suspend fun LocationDatabase(locationDao: LocationDao) {    //초기 데이터 넣어주기.(ex.서버)
            //LocationDao.insert(LocationEntity("Lilly","여",1))
        }
    }

}