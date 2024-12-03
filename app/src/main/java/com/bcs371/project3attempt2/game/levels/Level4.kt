package com.bcs371.project3attempt2.game.levels

import com.bcs371.project3attempt2.game.Arrow
import com.bcs371.project3attempt2.game.LevelConfiguration

val Level4 = LevelConfiguration(
    gridLayout = listOf(
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, true, true, true, true, false, false, false, true, true, true, false, false, false),
        listOf(false, false, false, false, false, true, false, false, false, true, false, true, false, false, false),
        listOf(false, false, false, false, false, true, true, true, true, true, false, true, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, true, false, false, false),
        listOf(false, false, true, true, true, true, true, true, true, true, true, true, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)
    ),
    startPosition = Pair(2, 5),
    endPosition = Pair(2, 1),
    inventory = listOf(
        Arrow.RIGHT,
        Arrow.UP,
        Arrow.UP,
        Arrow.LEFT,
        Arrow.LEFT,
        Arrow.LEFT,
        Arrow.DOWN
    )
) 