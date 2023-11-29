package com.example.learnwithpierre.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item : FlashCard)

    @Update
    suspend fun update(item: FlashCard)

    @Delete
    suspend fun delete(item : FlashCard)

    @Query("SELECT * from card WHERE cardId = :id")
    fun getCardById(id: Int): Flow<FlashCard>

    @Query("SELECT * from card WHERE recto = :content OR verso = :content")
    fun getCardByContent(content: String): Flow<FlashCard>

    @Query("SELECT * from card WHERE category = :category")
    fun getCardByCategory(category: String): Flow<MutableList<FlashCard>>

    @Query("SELECT * FROM card WHERE cardId IN (SELECT cardId FROM card ORDER BY RANDOM() LIMIT 10)")
    fun getRandomCard():  Flow<List<FlashCard>>


    @Query("SELECT * from card ORDER BY recto ASC")
    fun getAllCard(): Flow<MutableList<FlashCard>>

    @Query("SELECT DISTINCT category from card ORDER BY cardId desc")
    fun getCategories(): Flow<MutableList<String>>

    @Query("SELECT * FROM card WHERE deckId = :deckId")
    fun getCardByDeckId(deckId : Long): Flow<MutableList<FlashCard>>


}
