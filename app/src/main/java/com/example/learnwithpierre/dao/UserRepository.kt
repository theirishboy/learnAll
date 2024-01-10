package com.example.learnwithpierre.dao

interface UserRepository {
    suspend fun insertUser(user: User) : Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(userId: Long) : User
}