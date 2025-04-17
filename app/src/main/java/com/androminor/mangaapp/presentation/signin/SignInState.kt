package com.androminor.mangaapp.presentation.signin

data class SignInState(
    val email:String ="",
    val password:String ="",
    val isLoading: Boolean = false,
    val error:String? = null,
    val isPasswordVisible:Boolean = false
)
