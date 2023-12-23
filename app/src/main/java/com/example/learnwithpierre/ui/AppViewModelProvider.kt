package com.example.learnwithpierre.ui

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


import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learnwithpierre.LearnApplication
import com.example.learnwithpierre.model.AuthModule
import com.example.learnwithpierre.model.AuthRepositoryImpl
import com.example.learnwithpierre.ui.screen.AuthViewModel
import com.example.learnwithpierre.ui.screen.CardEntryViewModel
import com.example.learnwithpierre.ui.screen.HomeViewModel
import com.example.learnwithpierre.ui.screen.OneCardViewModel
import com.example.learnwithpierre.ui.screen.OneDeckViewModel
import com.example.learnwithpierre.ui.screen.OneDeckViewScreen
import com.example.learnwithpierre.ui.screen.ShowAllCardsScreenViewModel
import com.example.learnwithpierre.ui.screen.TrainViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
@AndroidEntryPoint

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeVewiewModel
        initializer {
            HomeViewModel(learnApplication().container.decksRepository,
                learnApplication().container.usersRepository)
        }
        initializer {
           CardEntryViewModel(learnApplication().container.cardsRepository,
               learnApplication().container.usersRepository,
               learnApplication().container.decksRepository)
        }
        initializer {
           TrainViewModel(learnApplication().container.cardsRepository, this.createSavedStateHandle())
        }
        initializer {
            ShowAllCardsScreenViewModel(learnApplication().container.cardsRepository)
        }
        initializer {
            OneDeckViewModel(
                learnApplication().container.decksRepository,
                learnApplication().container.cardsRepository,
                this.createSavedStateHandle(),
            )
        }
        initializer {
            OneCardViewModel(
                learnApplication().container.cardsRepository,
                this.createSavedStateHandle(),
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [learnApplication].
 */
fun CreationExtras.learnApplication(): LearnApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LearnApplication)
