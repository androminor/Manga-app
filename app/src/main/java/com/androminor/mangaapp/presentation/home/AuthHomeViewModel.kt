package com.androminor.mangaapp.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androminor.mangaapp.domain.usecase.auth.GetLoggedInUserUseCase
import com.androminor.mangaapp.domain.usecase.auth.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Varun Singh
 */
@HiltViewModel
class AuthHomeViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    private val _isLoggedOut = mutableStateOf(false)
    val isLoggedOut: State<Boolean> get() = _isLoggedOut


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