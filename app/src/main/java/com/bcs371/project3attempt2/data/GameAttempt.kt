package com.bcs371.project3attempt2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_attempts")
data class GameAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val childId: Int,
    val numberOfTries: Int,
    val isHardMode: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)