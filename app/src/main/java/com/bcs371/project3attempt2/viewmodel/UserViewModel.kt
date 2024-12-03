package com.bcs371.project3attempt2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bcs371.project3attempt2.data.AppDatabase
import com.bcs371.project3attempt2.data.User
import com.bcs371.project3attempt2.game.GameStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val context: Context) : ViewModel() {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val foundUser = userDao.getUser(username, password)
                if (foundUser != null) {
                    _currentUser.value = foundUser
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("Invalid username or password")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun register(username: String, password: String, isChild: Boolean, parentId: Int? = null) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    username = username,
                    password = password,
                    isChild = isChild,
                    parentId = parentId
                )
                userDao.insertUser(newUser)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun clearLoginError() {
        _loginState.value = LoginState.Idle
    }

    fun clearCurrentUser() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
        GameStateManager.clearCurrentUser()
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
} 