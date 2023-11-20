package com.example.learnwithpierre.dao
/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Card] from a given data source.
 */
interface DataRepository {
    /**
     * Retrieve all the Datas from the the given data source.
     */
    fun getAllDataStream(): Flow<MutableList<Card>>

    /**
     * Retrieve an Data from the given data source that matches with the [id].
     */
    fun getDataStream(id: Int): Flow<Card?>

    /**
     * Insert Data in the data source
     */
    suspend fun insertData(Card: Card)

    /**
     * Delete Data from the data source
     */
    suspend fun deleteData(Card: Card)

    /**
     * Update Data in the data source
     */
    suspend fun updateData(Card: Card)

    fun getRandomData():Flow<List<Card>>

    fun getCategories(): Flow<MutableList<String>>
    fun getDataByCategory(category: String): Flow<MutableList<Card>>
}
