package com.example.learnwithpierre.dao

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CardDatabase {
        return Room.databaseBuilder(
            appContext,
            CardDatabase::class.java,
            "my_database_name"
        ).build()
    }


    @Provides
    fun provideUserDao(database: CardDatabase): FlashCardDao {
        return database.CardDao()
    }
    @Provides
    fun provideDeckDao(database: CardDatabase): DeckDao {
        return database.DeckDao()
    }
    @Provides
    fun provideFlashCardDao(database: CardDatabase): UserDao {
        return database.UserDao()
    }
}