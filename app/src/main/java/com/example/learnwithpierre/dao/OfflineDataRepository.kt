package com.example.learnwithpierre.dao

import kotlinx.coroutines.flow.Flow

class OfflineDataRepository(private val dataDao: DataDao) : DataRepository {

    override fun getAllDataStream(): Flow<MutableList<Card>> = dataDao.getAllData()

    override fun getDataStream(id: Int): Flow<Card?> = dataDao.getDataById(id)

    override suspend fun insertData(Card: Card) = dataDao.insert(Card)

    override suspend fun deleteData(Card: Card) = dataDao.delete(Card)

    override suspend fun updateData(Card: Card) = dataDao.update(Card)

    override fun getRandomData(): Flow<List<Card>>  = dataDao.getRandomData()

    override fun getCategories(): Flow<MutableList<String>> = dataDao.getCategories()

    override fun getDataByCategory(category: String): Flow<MutableList<Card>> = dataDao.getDataByCategory(category)
}