package com.bcs371.project3attempt2.game.levels

import com.bcs371.project3attempt2.game.Arrow
import com.bcs371.project3attempt2.game.LevelConfiguration

val Level2 = LevelConfiguration(
    gridLayout = listOf(
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, true, true, true, true, true, false, false, false, false, false),
        listOf(false, false, true, true, true, true, false, false, false, true, true, false, false, false, false),
        listOf(false, false, true, false, false, true, true, true, true, true, false, false, false, false, false),
        listOf(false, false, true, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, true, false, false, false, false, false, false, false, false, false, false, false, false),
        listOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)
    ),
    startPosition = Pair(2, 6),
    endPosition = Pair(10, 3),
    inventory = listOf(
        Arrow.RIGHT,
        Arrow.RIGHT,
        Arrow.UP,
        Arrow.RIGHT
    )
).also {
    println("Level2 loaded with:")
    println("Start position: ${it.startPosition}")
    println("End position: ${it.endPosition}")
    println("Inventory size: ${it.inventory.size}")
    println("Inventory: ${it.inventory}")
} 