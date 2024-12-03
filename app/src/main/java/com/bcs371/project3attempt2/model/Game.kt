package com.bcs371.project3attempt2.model

enum class Difficulty {
    EASY,
    HARD
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class GameLevel(
    val id: Int,
    val difficulty: Difficulty,
    val startPosition: Pair<Int, Int>,
    val endPosition: Pair<Int, Int>,
    val gridSize: Int,
    val obstacles: List<Pair<Int, Int>> = emptyList()
) 