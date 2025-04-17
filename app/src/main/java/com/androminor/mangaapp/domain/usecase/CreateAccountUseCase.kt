package com.androminor.mangaapp.domain.usecase

import android.annotation.SuppressLint
import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class CreateAccountUseCase @Inject constructor(private val userRepository:UserRepository) {
    @SuppressLint("SuspiciousIndentation")
    suspend operator fun invoke(email:String, password:String):Result<User>{
    val existing  = userRepository.getUserByEmail(email)
        return if (existing!=null){
            Result.failure(Exception("Email already exists"))
        }
        else {
            val user  = User(email = email, password = password)
            val id = userRepository.insertUser(user)
            val newUser = user.copy(id = id)
            userRepository.logOutUser()
            userRepository.saveLoggedUser(newUser)
            Result.success(newUser)
        }

    }

}