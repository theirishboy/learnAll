package com.example.learnwithpierre.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

interface DeckRepository {
    suspend fun insertDeck(deck: Deck)
    suspend fun updateDeck(deck: Deck)
    suspend fun deleteDeck(deck: Deck)
    fun getAllDeckFromAUser(userId : Long) : Flow<MutableList<Deck>>
    fun getSizeOfADeck(deck : Long) : Long
}