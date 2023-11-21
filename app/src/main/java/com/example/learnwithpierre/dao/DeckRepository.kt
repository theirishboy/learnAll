package com.example.learnwithpierre.dao

import androidx.room.Dao

interface DeckRepository {
    suspend fun insertDeck(deck: Deck)
    suspend fun updateDeck(deck: Deck)
    suspend fun deleteDeck(deck: Deck)
}