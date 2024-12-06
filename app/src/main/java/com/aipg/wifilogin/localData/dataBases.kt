package com.aipg.wifilogin.localData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(dataClass::class), version = 1, exportSchema = false)
abstract class DataBases: RoomDatabase() {
    abstract fun getDataDao(): dataDao

    companion object {

        @Volatile
        private var INSTANCE: DataBases? = null
        fun getDataBase(context: Context): DataBases {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBases::class.java,
                    "AppDataBaseOld"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}