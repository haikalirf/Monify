package com.bleh.monify.feature_auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isUserLoggedIn: Boolean = false,
    val emailState: String = "",
    val passwordState: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUiClient: GoogleAuthClient,
    private val userDao: UserDao
): ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        checkLogin()
    }

    fun updateEmailState (email: String) {
        _state.update {
            it.copy(emailState = email)
        }
    }

    fun updatePasswordState (password: String) {
        _state.update {
            it.copy(passwordState = password)
        }
    }

    suspend fun onLoginClick(context: Context): LoginResult {
        val emailState = _state.value.emailState
        val passwordState = _state.value.passwordState
        if (emailState.isEmpty() || passwordState.isEmpty()) {
            Toast.makeText(
                context,
                "Please enter email and password",
                Toast.LENGTH_SHORT
            ).show()
        }
        return authUiClient.login(emailState, passwordState)
    }

    suspend fun onSignUpClick(context: Context): RegisterResult {
        val emailState = _state.value.emailState
        val passwordState = _state.value.passwordState
        if (emailState.isEmpty() || passwordState.isEmpty()) {
            Toast.makeText(
                context,
                "Please enter email and password",
                Toast.LENGTH_SHORT
            ).show()
        }
        return authUiClient.register(emailState, passwordState)
    }

    fun onLoginResult (result: LoginResult) {
        _state.update {
            it.copy(
                isUserLoggedIn = result.data != null,
            )
        }
    }

    private fun checkLogin () {
        val userData = authUiClient.getLoggedInUser()
        _state.update {
            it.copy(
                isUserLoggedIn = userData != null,
            )
        }
    }

    fun upsertUser(user: User) {
        viewModelScope.launch {
            userDao.upsertUser(
                user
            )
        }
    }

    fun resetState() {
        _state.value = AuthState()
    }
}