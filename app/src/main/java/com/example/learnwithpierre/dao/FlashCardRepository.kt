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
 * Repository that provides insert, update, delete, and retrieve of [FlashCard] from a given Card source.
 */
interface FlashCardRepository {
    /**
     * Retrieve all the Cards from the the given Card source.
     */
    fun getAllCardStream(): Flow<MutableList<FlashCard>>

    /**
     * Retrieve an Card from the given Card source that matches with the [id].
     */
    fun getCardStream(id: Long): Flow<FlashCard?>

    /**
     * Insert Card in the Card source
     */
    suspend fun insertCard(flashCard: FlashCard)

    /**
     * Delete Card from the Card source
     */
    suspend fun deleteCard(flashCard: FlashCard)

    /**
     * Update Card in the Card source
     */
    suspend fun updateCard(flashCard: FlashCard)

    fun getRandomCard():Flow<List<FlashCard>>

    fun getCategories(): Flow<MutableList<String>>
    fun getCardByCategory(category: String): Flow<MutableList<FlashCard>>

    fun getCardByDeckId(deckId : Long):Flow<MutableList<FlashCard>>
}
