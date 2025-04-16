package com.androminor.mangaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androminor.mangaapp.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean = false
) {
    fun toUser(): User {
        return User(
            id = id,
            email = email,
            password = password
        )
    }
}
