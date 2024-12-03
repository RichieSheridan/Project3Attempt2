package com.bcs371.project3attempt2.game

data class LevelConfiguration(
    val gridLayout: List<List<Boolean>>,
    val startPosition: Pair<Int, Int>,
    val endPosition: Pair<Int, Int>,
    val inventory: List<Arrow>,
    val setupSquareCount: Int = 5
)