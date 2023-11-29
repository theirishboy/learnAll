package com.example.learnwithpierre

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
import android.content.Context
import com.example.learnwithpierre.dao.CardDatabase
import com.example.learnwithpierre.dao.FlashCardRepository
import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.dao.DeckRepositoryOffline
import com.example.learnwithpierre.dao.OfflineFlashCardRepository
import com.example.learnwithpierre.dao.UserRepository
import com.example.learnwithpierre.dao.UserRepositoryOffline

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val cardsRepository: FlashCardRepository
    val usersRepository: UserRepository
    val decksRepository: DeckRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppCardContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [cardsRepository]
     */
    override val cardsRepository: FlashCardRepository by lazy {
        OfflineFlashCardRepository(CardDatabase.getDatabase(context).CardDao())
    }
    override val usersRepository: UserRepository by lazy {
        UserRepositoryOffline(CardDatabase.getDatabase(context).UserDao())
    }
    override val decksRepository: DeckRepository by lazy {
        DeckRepositoryOffline(CardDatabase.getDatabase(context).DeckDao())
    }

}