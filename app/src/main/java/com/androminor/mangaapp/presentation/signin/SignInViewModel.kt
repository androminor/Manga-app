package com.androminor.mangaapp.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androminor.mangaapp.domain.usecase.CheckUserLoginStatusUseCase
import com.androminor.mangaapp.domain.usecase.CreateAccountUseCase
import com.androminor.mangaapp.domain.usecase.SignInUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val checkUserLoginStatusUseCase: CheckUserLoginStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    private val state: StateFlow<SignInState> = _state.asStateFlow()

    private val _isSignInSuccessful = MutableStateFlow(false)
    val isSignInSuccessful: StateFlow<Boolean> = _isSignInSuccessful.asStateFlow()

    suspend fun checkLoginStatus(): Boolean {
        return checkUserLoginStatusUseCase()
    }

    fun onEvent(event: SignInEvent) {
        when (event) {
            is StatefulEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }

            }

            is StatefulEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }

            is BehaviouralEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            }

            is BehaviouralEvent.SignIn -> {
                singIn()
            }

            is BehaviouralEvent.ForgetPassword -> {
                //future work
            }

            is BehaviouralEvent.GoogleSignIn -> {
                //future work
            }

            is BehaviouralEvent.AppleSignIn -> {
                //future work
            }
        }
    }

    private fun singIn() {
        val email = state.value.email
        val password = state.value.password

        if (email.isBlank()) {
            _state.update { it.copy(error = "Email cannot be empty") }
            return
        }
        if (password.isBlank()) {
            _state.update { it.copy(error = "Password cannot be empty") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val result = signInUseCase(email, password)
                if (result.isSuccess) {
                    //updating the state
                    _isSignInSuccessful.value = true
                } else {
                    when (result.exceptionOrNull()?.message) {
                        "User not their" -> {
                            val createAccountResult = createAccountUseCase(email, password)
                            if (createAccountResult.isSuccess) {
                                _isSignInSuccessful.value = true
                            } else {
                                _state.update {
                                    it.copy(
                                        error = createAccountResult.exceptionOrNull()?.message
                                            ?: "Failed to create account",
                                        isLoading = false

                                    )
                                }
                            }
                        }

                        else -> _state.update {
                            it.copy(
                                error = result.exceptionOrNull()?.message
                                    ?: "Authentication failed",
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: CancellationException) {
//Propagating cancellation if occurred so that coroutine is cancelled nicely
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Unknown exception",
                        isLoading = false
                    )
                }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

}