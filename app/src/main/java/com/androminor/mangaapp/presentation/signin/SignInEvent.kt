package com.androminor.mangaapp.presentation.signin

sealed interface SignInEvent

sealed class StatefulEvent : SignInEvent {
    data class EmailChanged(val email: String) : StatefulEvent()
    data class PasswordChanged(val password: String) : StatefulEvent()

}

sealed interface BehaviouralEvent : SignInEvent {
    data object TogglePasswordVisibility : BehaviouralEvent
    data object SignIn : BehaviouralEvent
    data object ForgetPassword : BehaviouralEvent
    data object GoogleSignIn : BehaviouralEvent
    data object AppleSignIn : BehaviouralEvent
    data object SignUp:BehaviouralEvent
}