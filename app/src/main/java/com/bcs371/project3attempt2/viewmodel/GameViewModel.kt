package com.bcs371.project3attempt2.viewmodel

import androidx.lifecycle.ViewModel
import com.bcs371.project3attempt2.model.Difficulty
import com.bcs371.project3attempt2.model.Direction
import com.bcs371.project3attempt2.model.GameLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow<GameState>(GameState.Initial)
    val gameState: StateFlow<GameState> = _gameState

    private val easyLevels = listOf(
        GameLevel(1, Difficulty.EASY, Pair(0, 0), Pair(2, 2), 3),
        GameLevel(2, Difficulty.EASY, Pair(0, 0), Pair(3, 3), 4),
        GameLevel(3, Difficulty.EASY, Pair(0, 0), Pair(4, 4), 5)
    )

    private val hardLevels = listOf(
        GameLevel(1, Difficulty.HARD, Pair(0, 0), Pair(3, 3), 4, listOf(Pair(1, 1), Pair(2, 2))),
        GameLevel(2, Difficulty.HARD, Pair(0, 0), Pair(4, 4), 5, listOf(Pair(2, 2), Pair(3, 3))),
        GameLevel(3, Difficulty.HARD, Pair(0, 0), Pair(5, 5), 6, listOf(Pair(2, 2), Pair(3, 3), Pair(4, 4)))
    )

    fun startGame(difficulty: Difficulty) {
        val levels = if (difficulty == Difficulty.EASY) easyLevels else hardLevels
        _gameState.value = GameState.Playing(levels.first())
    }

    fun submitSolution(directions: List<Direction>) {}
}

sealed class GameState {
    object Initial : GameState()
    data class Playing(val level: GameLevel) : GameState()
    data class Complete(val success: Boolean) : GameState()
} 