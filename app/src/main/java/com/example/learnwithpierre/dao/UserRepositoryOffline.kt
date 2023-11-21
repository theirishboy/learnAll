package com.example.learnwithpierre.dao

class UserRepositoryOffline(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User)  = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

}