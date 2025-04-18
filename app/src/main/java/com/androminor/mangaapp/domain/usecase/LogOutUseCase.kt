package com.androminor.mangaapp.domain.usecase

import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class LogOutUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User){
        return userRepository.logOutUser(user)
    }

}