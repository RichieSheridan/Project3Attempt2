package com.bcs371.project3attempt2.game

import android.util.Log
import com.bcs371.project3attempt2.data.GameAttempt
import com.bcs371.project3attempt2.data.User
import com.bcs371.project3attempt2.data.UserDao
import com.bcs371.project3attempt2.game.levels.Level1
import com.bcs371.project3attempt2.game.levels.Level4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GameStateManager {
    private var currentLevel = 1
    private var _isHardMode = false
    
    @JvmStatic
    private val _easyAttempts = mutableListOf<Int>()
    @JvmStatic
    private val _hardAttempts = mutableListOf<Int>()
    
    val isHardMode: Boolean get() = _isHardMode
    val easyAttempts: List<Int> get() = _easyAttempts.toList()
    val hardAttempts: List<Int> get() = _hardAttempts.toList()
    
    private lateinit var userDao: UserDao
    private var currentUser: User? = null
    
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow: StateFlow<Int> = _stateFlow.asStateFlow()
    
    fun initialize(userDao: UserDao) {
        this.userDao = userDao
    }
    
    fun setCurrentUser(user: User) {
        currentUser = user
    }
    
    fun clearCurrentUser() {
        currentUser = null
    }
    
    suspend fun recordAttempt(tries: Int, isHardMode: Boolean) {
        currentUser?.let { user ->
            userDao.insertAttempt(
                GameAttempt(
                    childId = user.id,
                    numberOfTries = tries,
                    isHardMode = isHardMode
                )
            )
        }
        if (isHardMode) {
            _hardAttempts.add(tries)
        } else {
            _easyAttempts.add(tries)
        }
        _stateFlow.value++
        
        Log.d("GameStateManager", "Recorded ${if (isHardMode) "hard" else "easy"} mode attempt: $tries")
        Log.d("GameStateManager", "Current attempts - Easy: $_easyAttempts, Hard: $_hardAttempts")
    }
    
    fun getCurrentLevel(): LevelConfiguration {
        return if (_isHardMode) {
            Level4
        } else {
            Level1
        }
    }
    
    fun nextLevel(): Boolean {
        return false
    }
    
    fun resetProgress() {
        currentLevel = 1
    }
    
    fun setHardMode(enabled: Boolean) {
        _isHardMode = enabled
        currentLevel = 1
        Log.d("GameStateManager", "Hard mode set to: $enabled")
    }
    
    fun isLastLevel() = true  //Now always true since I reduced to 2 levels
}