package com.bcs371.project3attempt2.game.levels

import com.bcs371.project3attempt2.game.Arrow
import com.bcs371.project3attempt2.game.LevelConfiguration

val Level1 = LevelConfiguration(
    gridLayout = listOf(
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, true, true, true, true, false),
        listOf(false, false, false, false, false, false, false, false, false, false, true, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, true, false, false, false, false),
        listOf(false, false, false, true, true, true, true, true, true, true, true, false, false, false, false),
        listOf(false, false, false, true, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, true, false, false, false, false, false, false, false, false, false, false, false)
    ),
    startPosition = Pair(3, 6),
    endPosition = Pair(13, 1),
    inventory = listOf(
        Arrow.RIGHT,
        Arrow.RIGHT,
        Arrow.RIGHT,
        Arrow.UP,
        Arrow.UP,
        Arrow.RIGHT
    )
) 