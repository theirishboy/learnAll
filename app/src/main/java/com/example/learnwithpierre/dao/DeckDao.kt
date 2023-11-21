package com.example.learnwithpierre.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface DeckDao {
    @Insert
    fun insertDeck(deck: Deck)

    @Update
    fun updateDeck(deck: Deck)

    @Delete
    fun deleteDeck(deck: Deck)
}