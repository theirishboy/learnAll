package com.example.learnwithpierre.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface DeckRepository {
    suspend fun insertDeck(deck: Deck)
    suspend fun updateDeck(deck: Deck)
    suspend fun deleteDeck(deck: Deck)
    suspend fun deleteDeckById(deckId: Long)
    suspend fun getDeck(deckId: Long) : Deck
    fun getAllDeckFromAUser(userId : Long) : Flow<MutableList<Deck>>
    fun getSizeOfADeck(deck : Long) : Long
    fun updateDateModificationByDeckById(deckId: Long,dateModification : LocalDateTime)
}