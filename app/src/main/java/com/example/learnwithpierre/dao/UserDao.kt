package com.example.learnwithpierre.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    //Long allow us to retrieve the id of the new created user
    @Insert
    suspend fun insert(user: User):Long
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)
    @Query("SELECT * FROM USERS WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User
}