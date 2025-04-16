package com.androminor.mangaapp.domain.repository

import com.androminor.mangaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signIn(email:String,password:String):Result<User>
    suspend fun createAccount(email:String,password: String):Result<User>
    fun getLoggedInUser(): Flow<User?>
    suspend fun setUserLoggedIn(user:User)
    suspend fun isUserLoggedIn():Boolean
    suspend fun logOutUser()

}