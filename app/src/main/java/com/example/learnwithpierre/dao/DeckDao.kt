package com.example.learnwithpierre.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Insert
    fun insertDeck(deck: Deck)

    @Update
    fun updateDeck(deck: Deck)

    @Delete
    fun deleteDeck(deck: Deck)

    @Query("SELECT * FROM DECKS WHERE :userId = userId")
    fun getAllDeckFromAUser(userId: Long): Flow<MutableList<Deck>>

    @Query("SELECT * FROM DECKS WHERE :userId = userId")
    fun getAllDeckFromAUserAndNumberOfCard(userId: Long): Flow<MutableList<Deck>>

    @Query("SELECT COUNT(*) FROM CARD WHERE :deckId = deckId")
    fun getSizeOfADeck(deckId : Long): Long
}