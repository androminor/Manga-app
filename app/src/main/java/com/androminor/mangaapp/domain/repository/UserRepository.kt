package com.androminor.mangaapp.domain.repository

import com.androminor.mangaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserByEmail(email:String):User?
    suspend fun insertUser(user: User):Long
    fun getLoggedInUser(): Flow<User?>
    suspend fun saveLoggedUser(user:User)
    suspend fun isUserLoggedIn():Boolean
    suspend fun logOutUser()

}