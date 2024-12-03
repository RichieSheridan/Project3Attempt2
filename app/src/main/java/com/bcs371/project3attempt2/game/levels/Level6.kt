package com.bcs371.project3attempt2.game.levels

import com.bcs371.project3attempt2.game.Arrow
import com.bcs371.project3attempt2.game.LevelConfiguration

val Level6 = LevelConfiguration(
    gridLayout = listOf(
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)
    ),
    startPosition = Pair(0, 0),
    endPosition = Pair(0, 0),
    inventory = listOf(
        Arrow.RIGHT,
        Arrow.UP,
        Arrow.LEFT,
        Arrow.DOWN,
        Arrow.RIGHT,
        Arrow.UP
    )
) 