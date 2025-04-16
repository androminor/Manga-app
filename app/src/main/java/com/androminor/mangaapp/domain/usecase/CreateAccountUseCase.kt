package com.androminor.mangaapp.domain.usecase

import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class CreateAccountUseCase @Inject constructor(private val userRepository:UserRepository) {
    suspend operator fun invoke(email:String,password:String):Result<User>{
        return userRepository.createAccount(email,password)
    }

}