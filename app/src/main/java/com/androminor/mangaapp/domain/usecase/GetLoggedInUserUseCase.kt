package com.androminor.mangaapp.domain.usecase

import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class GetLoggedInUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<User?> {
        return userRepository.getLoggedInUser()
    }

}