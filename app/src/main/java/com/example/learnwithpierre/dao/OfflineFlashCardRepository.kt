package com.example.learnwithpierre.dao

import kotlinx.coroutines.flow.Flow

class OfflineFlashCardRepository(private val flashCardDao: FlashCardDao) : FlashCardRepository {

    override fun getAllCardStream(): Flow<MutableList<FlashCard>> = flashCardDao.getAllCard()

    override fun getCardStream(id: Int): Flow<FlashCard?> = flashCardDao.getCardById(id)

    override suspend fun insertCard(FlashCard: FlashCard) = flashCardDao.insert(FlashCard)

    override suspend fun deleteCard(FlashCard: FlashCard) = flashCardDao.delete(FlashCard)

    override suspend fun updateCard(FlashCard: FlashCard) = flashCardDao.update(FlashCard)

    override fun getRandomCard(): Flow<List<FlashCard>>  = flashCardDao.getRandomCard()

    override fun getCategories(): Flow<MutableList<String>> = flashCardDao.getCategories()

    override fun getCardByCategory(category: String): Flow<MutableList<FlashCard>> = flashCardDao.getCardByCategory(category)
}