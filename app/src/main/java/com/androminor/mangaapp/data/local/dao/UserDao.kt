package com.androminor.mangaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androminor.mangaapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email =:email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserId(id:Long):UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UserEntity>

    @Query("Update users SET isLoggedIn =1 WHERE id = :userId ")
    suspend fun setUserLoggedIn(userId: Long)

    @Query("UPDATE users SET isLoggedIn= 0")
    suspend fun logOutAllUsers()

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE isLoggedIn = 1)")
    suspend fun isAnyUserLoggedIn(): Boolean
}