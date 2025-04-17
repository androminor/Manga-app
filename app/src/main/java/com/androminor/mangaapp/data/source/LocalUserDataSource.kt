package com.androminor.mangaapp.data.source

import com.androminor.mangaapp.data.local.dao.UserDao
import com.androminor.mangaapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class LocalUserDataSource @Inject constructor(private val userDao: UserDao) {

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    fun getLoggedInUser(): Flow<UserEntity?> {
        return userDao.getLoggedInUser()
    }

    suspend fun saveLoggedIn(userId: Long) {
        userDao.setUserLoggedIn(userId)
    }

    suspend fun logOutUser() {
        userDao.logOutAllUsers()
    }

    suspend fun isAnyUserLoggedIn():Boolean {
        return userDao.isAnyUserLoggedIn()
    }
}