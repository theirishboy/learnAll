package com.example.learnwithpierre.dao

import javax.inject.Inject

class UserRepositoryOffline @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User)  = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)
    override suspend fun getUserById(userId: Long) = userDao.getUserById(userId)

}