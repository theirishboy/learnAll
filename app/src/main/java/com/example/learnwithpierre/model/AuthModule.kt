package com.example.learnwithpierre.model

import com.example.learnwithpierre.dao.DeckRepository
import com.example.learnwithpierre.dao.DeckRepositoryOffline
import com.example.learnwithpierre.dao.FlashCardRepository
import com.example.learnwithpierre.dao.OfflineFlashCardRepository
import com.example.learnwithpierre.dao.UserRepository
import com.example.learnwithpierre.dao.UserRepositoryOffline
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

}