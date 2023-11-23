package com.example.learnwithpierre.dao

class DeckRepositoryOffline(private val deckDao: DeckDao): DeckRepository {
    override suspend fun insertDeck(deck: Deck) = deckDao.insertDeck(deck)
    override suspend fun updateDeck(deck: Deck) = deckDao.updateDeck(deck)
    override suspend fun deleteDeck(deck: Deck) = deckDao.deleteDeck(deck)
    override fun getAllDeckFromAUser(userId : Long) = deckDao.getAllDeckFromAUser(userId)
    override fun getSizeOfADeck(deckId: Long): Long = deckDao.getSizeOfADeck(deckId)
}