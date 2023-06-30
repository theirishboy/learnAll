package com.example.learnwithpierre.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

//increase the number of version each time we modify the database
@Database(entities = [Data::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DataDatabase : RoomDatabase() {
    //to make the database identify the DAO
    abstract fun DataDao(): DataDao
    //allow to access method insert... or to generate the database
    companion object {
        //Every writing and reading come from the main memory so Instance is always up to date
        @Volatile
        private var Instance: DataDatabase? = null
        fun getDatabase(context: Context): DataDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DataDatabase::class.java, "data_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }

            }
        }

    }



}

