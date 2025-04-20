package com.androminor.mangaapp.domain.usecase.auth

import com.androminor.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class CheckUserLoginStatusUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke():Boolean = userRepository.isUserLoggedIn()

}