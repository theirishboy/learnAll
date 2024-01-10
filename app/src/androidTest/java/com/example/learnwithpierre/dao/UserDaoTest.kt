package com.example.learnwithpierre.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var cardDatabase: CardDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        cardDatabase = Room.inMemoryDatabaseBuilder(
            context, CardDatabase::class.java).build()
        userDao = cardDatabase.UserDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        cardDatabase.close()
    }

    @Test
    fun testInsertUser() = runBlocking{
        val user =  User(1,"Test Name", "Email")
        userDao.insert(user = user)
        val result = userDao.getUserById(user.userId)
        assertEquals(user,result)
    }
    @Test
    fun testUpdateUser() = runBlocking{
        val user =  User(1,"Test Name", "Email")
        val userUpdated =  User(1,"Test Name Updated", "Email")
        userDao.insert(user = user)
        userDao.update(user = userUpdated)
        val result = userDao.getUserById(user.userId)
        assertEquals(userUpdated,result)
    }
    @Test
    fun deleteUser() = runBlocking {
        val user =  User(1,"Test Name", "Email")
        userDao.delete(user = user)
        val result = userDao.getUserById(user.userId)
        assertEquals(null,result)
    }
}