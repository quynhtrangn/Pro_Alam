package com.example.clock2.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ClockData::class], version = 1, exportSchema = false)
abstract class ClockDatabase  : RoomDatabase() {

    abstract  fun clockDao(): ClockDAO

    companion object{
        @Volatile
        private  var INSTANCE: ClockDatabase? = null
        fun getInstance(context:Context): ClockDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        fun getDatabase(context: Context) : ClockDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClockDatabase::class.java,
                    "clock_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
        private fun buildDatabase(context: Context): ClockDatabase {
            return Room.databaseBuilder(
                context,
                ClockDatabase::class.java,
                "alarm_database.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}