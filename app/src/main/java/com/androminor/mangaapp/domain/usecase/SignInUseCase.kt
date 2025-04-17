package com.androminor.mangaapp.domain.usecase

import android.annotation.SuppressLint
import com.androminor.mangaapp.domain.model.User
import com.androminor.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {
    @SuppressLint("SuspiciousIndentation")
    suspend operator fun invoke(email: String, password: String): Result<User> {
     val user  = userRepository.getUserByEmail(email)
        return when{
            user == null ->Result.failure(Exception("not found"))
            user.password != password -> Result.failure(Exception("Wrong password"))
            else -> {
                userRepository.logOutUser()
                userRepository.saveLoggedUser(user)
                Result.success(user)
            }
        }

    }

}