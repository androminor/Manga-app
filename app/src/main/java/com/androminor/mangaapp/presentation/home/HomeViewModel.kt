package com.androminor.mangaapp.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androminor.mangaapp.domain.usecase.GetLoggedInUserUseCase
import com.androminor.mangaapp.domain.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Varun Singh
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _isLoggedOut = mutableStateOf(false)
    val isLoggedOut: State<Boolean> get() = _isLoggedOut

    init {
        viewModelScope.launch {
            getLoggedInUserUseCase().collect { user ->
                _userName.value = user?.email
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val user = getLoggedInUserUseCase().firstOrNull()
            user?.let {
                logOutUseCase(it)
                _isLoggedOut.value = true
                Log.d("Debugging SignOut","SignOut completed. isLoggedOut = ${_isLoggedOut.value}")

            }


        }
    }
    fun resetLogOut(){
        _isLoggedOut.value = false
        Log.d("HomeViewModel", "Reset isLoggedOut = false")

    }


}