package com.example.learnwithpierre.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class DeckDaoTest {
        private lateinit var userDao: UserDao
        private lateinit var deckDao: DeckDao
        private lateinit var cardDao: FlashCardDao
        private lateinit var cardDatabase: CardDatabase

        @Before
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            cardDatabase = Room.inMemoryDatabaseBuilder(
                context, CardDatabase::class.java).build()
            userDao = cardDatabase.UserDao()
            deckDao = cardDatabase.DeckDao()
            cardDao = cardDatabase.CardDao()
            runBlocking {
                initDaoForDeck()
            }
        }
        private suspend fun initDaoForDeck() {
            userDao.insert(User(1,"testUserDaoName","fake"))
        }

        @After
        @Throws(IOException::class)
        fun closeDb() {
            cardDatabase.close()
        }
        @Test
        fun testInsertDeck() = runBlocking {
            val deck = Deck(1,1,"TestInsert","TestDescript")
            deckDao.insertDeck(deck)
            val result = deckDao.getAllDeckFromAUser(1).first() // Take the first emitted item
            assertEquals(listOf( deck), result)
        }
        @Test
        fun testUpdateDeck(){
            runBlocking {
                val deck = Deck(1,1,"TestInsert","TestDescript")
                val updatedDeck = Deck(1,1,"UpatedDeck","TestUpdatedDescription")
                deckDao.insertDeck(deck)
                deckDao.updateDeck(updatedDeck)
                val result = deckDao.getAllDeckFromAUser(1).first() // Take the first emitted item
                assertEquals(listOf( updatedDeck), result)
            }
        }
        @Test
         fun testDeleteDeck(){
            runBlocking {
                val deck = Deck(1,1,"TestInsert","TestDescript")
                deckDao.insertDeck(deck)
                deckDao.deleteDeck(deck)
                val result = deckDao.getAllDeckFromAUser(1).first() // Take the first emitted item
                assertEquals(emptyList<Deck>(),result)
            }
         }
        @Test
         fun testDeleteDeckById(){
            runBlocking {
                val deck1 = Deck(1,1,"TestInsert","TestDescript")
                val deck2 = Deck(2,1,"TestInsert","TestDescript")
                val deck3 = Deck(3,1,"TestInsert","TestDescript")
                deckDao.insertDeck(deck1)
                deckDao.insertDeck(deck2)
                deckDao.insertDeck(deck3)

                deckDao.deleteDeck(deck2)
                val result = deckDao.getAllDeckFromAUser(1).first() // Take the first emitted item
                assertEquals(listOf(deck1,deck3),result)
            }

         }
        @Test
        fun testGetDeckById() = runBlocking {
            val deck1 = Deck(1,1,"TestInsert","TestDescript")
            val deck2 = Deck(2,1,"TestInsert","TestDescript")
            val deck3 = Deck(3,1,"TestInsert","TestDescript")
            deckDao.insertDeck(deck1)
            deckDao.insertDeck(deck2)
            deckDao.insertDeck(deck3)

            val result = deckDao.getDeckById(2) // Take the first emitted item
            assertEquals(deck2,result)
        }
        @Test
        fun testGetAllDeckFromAUser() = runBlocking {
            userDao.insert(User(2,"testUserDaoName","fake"))
            val deck1 = Deck(1,1,"TestInsert","TestDescript")
            val deck2 = Deck(2,1,"TestInsert","TestDescript")
            val deck3 = Deck(3,1,"TestInsert","TestDescript")
            val deck4 = Deck(4,2,"TestInsert","TestDescript")
            val deck5 = Deck(5,2,"TestInsert","TestDescript")
            deckDao.insertDeck(deck1)
            deckDao.insertDeck(deck2)
            deckDao.insertDeck(deck3)
            deckDao.insertDeck(deck4)
            deckDao.insertDeck(deck5)

            val result = deckDao.getAllDeckFromAUser(2).first() // Take the first emitted item
            assertEquals(listOf(deck4,deck5),result)
        }
        @Test
        fun testGetSizeOfADeck() = runBlocking {
            val deck1 = Deck(1,1,"TestInsert","TestDescript")
            deckDao.insertDeck(deck1)
            cardDao.insert(FlashCard(1,1,"wdq","fqwefewfwe",true,"jeu",5))

            val result = deckDao.getSizeOfADeck(1) // Take the first emitted item
            assertEquals(1,result)
        }
        @Test
        fun testUpdateDateModificationByDeckById() = runBlocking {
            val deck1 = Deck(1,1,"TestInsert","TestDescript")
            deckDao.insertDeck(deck1)
            val newDate =  LocalDateTime.of(2022,5,5,5,5)
            deckDao.updateDateModificationByDeckId(1, newDate)
            val result = deckDao.getDeckById(1) // Take the first emitted item
            assertEquals(newDate,result.updatedAt)
        }

}