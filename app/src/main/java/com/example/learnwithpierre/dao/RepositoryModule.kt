package com.example.learnwithpierre.dao

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindFlashCardRepository(
        impl: OfflineFlashCardRepository
    ): FlashCardRepository
    @Binds
    abstract fun bindDeckRepository(
        impl: DeckRepositoryOffline
    ): DeckRepository
    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryOffline
    ): UserRepository
}