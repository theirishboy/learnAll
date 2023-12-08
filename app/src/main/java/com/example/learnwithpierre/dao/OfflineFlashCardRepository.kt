package com.example.learnwithpierre.dao

import kotlinx.coroutines.flow.Flow

class OfflineFlashCardRepository(private val flashCardDao: FlashCardDao) : FlashCardRepository {

    override fun getAllCardStream(): Flow<MutableList<FlashCard>> = flashCardDao.getAllCard()

    override fun getCardStream(id: Long): Flow<FlashCard?> = flashCardDao.getCardById(id)

    override suspend fun insertCard(flashCard: FlashCard) = flashCardDao.insert(flashCard)

    override suspend fun deleteCard(flashCard: FlashCard) = flashCardDao.delete(flashCard)

    override suspend fun updateCard(flashCard: FlashCard) = flashCardDao.update(flashCard)

    override fun getRandomCard(): Flow<MutableList<FlashCard>>  = flashCardDao.getRandomCard()

    override fun getCategories(): Flow<MutableList<String>> = flashCardDao.getCategories()

    override fun getCardByCategory(category: String): Flow<MutableList<FlashCard>> = flashCardDao.getCardByCategory(category)
    override fun getCardByDeckId(deckId : Long): Flow<MutableList<FlashCard>> = flashCardDao.getCardByDeckId(deckId)
}