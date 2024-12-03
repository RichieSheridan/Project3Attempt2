package com.bcs371.project3attempt2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_progress")
data class GameProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val childId: Int,
    val level: Int,
    val completed: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)