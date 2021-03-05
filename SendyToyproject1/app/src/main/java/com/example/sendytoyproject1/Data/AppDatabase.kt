package com.example.sendytoyproject1.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//앱데이타베이스(룸) -> 서버의 데이터를 가져오는 역할.
//이번 어플에서는 서버 안써서 필요없음.

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun LocationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.LocationDao())
                }
            }
        }
        //이번 어플에서는 서버 안써서 필요없음.
        suspend fun populateDatabase(userDao: LocationDao) {    //서버 데이터 가져오기.

            //userDao.deleteAll()
            // Add User
            //userDao.insert(LocationEntity("Lilly","여",1))
        }
    }

}