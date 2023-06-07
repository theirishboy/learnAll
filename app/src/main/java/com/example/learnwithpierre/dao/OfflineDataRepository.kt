package com.example.learnwithpierre.dao

import kotlinx.coroutines.flow.Flow

class OfflineDataRepository(private val dataDao: DataDao) : DataRepository {

    override fun getAllDataStream(): Flow<MutableList<Data>> = dataDao.getAllData()

    override fun getDataStream(id: Int): Flow<Data?> = dataDao.getDataById(id)

    override suspend fun insertData(Data: Data) = dataDao.insert(Data)

    override suspend fun deleteData(Data: Data) = dataDao.delete(Data)

    override suspend fun updateData(Data: Data) = dataDao.update(Data)

    override fun getRandomData(): Flow<List<Data>>  = dataDao.getRandomData()
}