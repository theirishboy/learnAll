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
    suspend fun insert(item : Card)

    @Update
    suspend fun update(item: Card)

    @Delete
    suspend fun delete(item : Card)

    @Query("SELECT * from card WHERE id = :id")
    fun getDataById(id: Int): Flow<Card>

    @Query("SELECT * from card WHERE recto = :content OR verso = :content")
    fun getDataByContent(content: String): Flow<Card>

    @Query("SELECT * from card WHERE category = :category")
    fun getDataByCategory(category: String): Flow<MutableList<Card>>

    @Query("SELECT * FROM card WHERE id IN (SELECT id FROM card ORDER BY RANDOM() LIMIT 10)")
    fun getRandomData():  Flow<List<Card>>


    @Query("SELECT * from card ORDER BY recto ASC")
    fun getAllData(): Flow<MutableList<Card>>

    @Query("SELECT DISTINCT category from card ORDER BY id desc")
    fun getCategories(): Flow<MutableList<String>>


}
