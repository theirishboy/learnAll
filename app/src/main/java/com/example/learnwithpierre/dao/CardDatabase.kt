package com.example.learnwithpierre.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//increase the number of version each time we modify the database
@Database(entities = [Card::class, Deck::class,User::class], version = 12, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CardDatabase : RoomDatabase() {
    //to make the database identify the DAO
    abstract fun CardDao(): CardDao
    abstract fun UserDao(): UserDao
    abstract fun DeckDao(): DeckDao
    //allow to access method insert... or to generate the database
    companion object {
        //Every writing and reading come from the main memory so Instance is always up to date
        @Volatile
        private var Instance: CardDatabase? = null
        fun getDatabase(context: Context): CardDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CardDatabase::class.java, "data_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }

            }
        }

    }



}

