package com.example.learnwithpierre.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : Data)

    @Update
    suspend fun update(item: Data)

    @Delete
    suspend fun delete(item : Data)

    @Query("SELECT * from data WHERE id = :id")
    fun getDataById(id: Int): Flow<Data>

    @Query("SELECT * from data WHERE recto = :content OR verso = :content")
    fun getDataByContent(content: String): Flow<Data>

    @Query("SELECT * from data WHERE category = :category")
    fun getDataByCategory(category: String): Flow<List<Data>>

    @Query("SELECT * FROM data WHERE id IN (SELECT id FROM data ORDER BY RANDOM() LIMIT 10)")
    fun getRandomData():  Flow<List<Data>>


    @Query("SELECT * from data ORDER BY recto ASC")
    fun getAllData(): Flow<MutableList<Data>>


}
