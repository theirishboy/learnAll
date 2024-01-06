package com.example.learnwithpierre.dao

import java.time.LocalDateTime
import javax.inject.Inject

class DeckRepositoryOffline @Inject constructor(private val deckDao: DeckDao): DeckRepository {
    override suspend fun getDeck(deckId: Long): Deck = deckDao.getDeck(deckId)
    override suspend fun insertDeck(deck: Deck) = deckDao.insertDeck(deck)
    override suspend fun updateDeck(deck: Deck) = deckDao.updateDeck(deck)
    override suspend fun deleteDeck(deck: Deck) = deckDao.deleteDeck(deck)
    override suspend fun deleteDeckById(deckId: Long) = deckDao.deleteDeckById(deckId)

    override fun getAllDeckFromAUser(userId : Long) = deckDao.getAllDeckFromAUser(userId)
    override fun getSizeOfADeck(deckId: Long): Long = deckDao.getSizeOfADeck(deckId)
    override fun updateDateModificationByDeckById(deckId: Long, dateModification : LocalDateTime) = deckDao.updateDateModificationByDeckId(deckId,dateModification)}