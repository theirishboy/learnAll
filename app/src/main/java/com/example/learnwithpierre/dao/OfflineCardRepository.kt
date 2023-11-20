package com.example.learnwithpierre.dao

import kotlinx.coroutines.flow.Flow

class OfflineCardRepository(private val cardDao: CardDao) : CardRepository {

    override fun getAllCardStream(): Flow<MutableList<Card>> = cardDao.getAllCard()

    override fun getCardStream(id: Int): Flow<Card?> = cardDao.getCardById(id)

    override suspend fun insertCard(Card: Card) = cardDao.insert(Card)

    override suspend fun deleteCard(Card: Card) = cardDao.delete(Card)

    override suspend fun updateCard(Card: Card) = cardDao.update(Card)

    override fun getRandomCard(): Flow<List<Card>>  = cardDao.getRandomCard()

    override fun getCategories(): Flow<MutableList<String>> = cardDao.getCategories()

    override fun getCardByCategory(category: String): Flow<MutableList<Card>> = cardDao.getCardByCategory(category)
}