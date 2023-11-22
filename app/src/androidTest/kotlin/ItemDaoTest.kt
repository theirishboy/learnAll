package com.example.learnwithpierre

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learnwithpierre.dao.Card
import com.example.learnwithpierre.dao.CardDao
import com.example.learnwithpierre.dao.CardDatabase
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private lateinit var cardDao: CardDao
    private lateinit var carddatabase: CardDatabase
    private var item1 = Card(1, 1,"Apples", "Pear", false,"fruit",1, LocalDateTime.now())
    private var item2 = Card(2, 1,"Bananas", "Peach", false,"fruit",1, LocalDateTime.now())

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        carddatabase = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        cardDao = carddatabase.CardDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        carddatabase.close()
    }
    private suspend fun addTwoItemsToDb() {
        cardDao.insert(item1)
        cardDao.insert(item2)
    }
    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = cardDao.getAllCard().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }
    @Test
    @Throws(Exception::class)
    fun daoGetRandomItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = cardDao.getRandomCard().first()
        assertNotNull(allItems[0])
        assertNotNull(allItems[1])
    }
    @Test
    @Throws(Exception::class)
    fun daoGetDataById() = runBlocking {
        addTwoItemsToDb()
        val item = cardDao.getCardById(1).first()
        assertEquals(item, item1)
    }
    @Test
    @Throws(Exception::class)
    fun getDataByContentRecto() = runBlocking {
        addTwoItemsToDb()
        val item = cardDao.getCardByContent("Apples").first()
        assertEquals(item, item1)
    }
    @Test
    @Throws(Exception::class)
    fun getDataByContentVerso() = runBlocking {
        addTwoItemsToDb()
        val item = cardDao.getCardByContent("Peach").first()
        assertEquals(item, item2)
    }
    @Test
    @Throws(Exception::class)
    fun getDataByCategory() = runBlocking {
        addTwoItemsToDb()
        val allItems = cardDao.getCardByCategory("fruit").first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }
    @Test
    @Throws(Exception::class)
    fun getCategories() = runBlocking {
        addTwoItemsToDb()
        val allItems = cardDao.getCategories().first()
        assertEquals(allItems[0], "fruit")
     //   assertEquals(allItems[1], item2)
    }








}
