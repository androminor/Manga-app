package com.androminor.mangaapp.data.repository

import com.androminor.mangaapp.data.local.entity.UserEntity
import com.androminor.mangaapp.data.source.LocalUserDataSource
import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class UserRepositoryImpl @Inject constructor(private val localDataSource: LocalUserDataSource) :
    UserRepository {

    override suspend fun getUserByEmail(email: String): User? {
        return localDataSource.getUserByEmail(email)?.toUser()
    }

    override suspend fun insertUser(user: User): Long {
        val entity = UserEntity(
            id = user.id,
            email = user.email,
            password = user.password,
            isLoggedIn = false
        )
        return localDataSource.insertUser(entity)
    }

    override fun getLoggedInUser(): Flow<User?> {
        return localDataSource.getLoggedInUser().map { it?.toUser() }
    }

    override suspend fun saveLoggedUser(user: User) {
        localDataSource.logOutUser()
        localDataSource.saveLoggedIn(user.id)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return localDataSource.isAnyUserLoggedIn()
    }

    override suspend fun logOutUser(user:User) {

        return localDataSource.logOutUser()
    }

}