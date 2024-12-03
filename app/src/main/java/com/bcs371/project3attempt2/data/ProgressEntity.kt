package com.bcs371.project3attempt2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_table")
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val timestamp: Long,
    val attempts: Int,
    val isHardMode: Boolean
) 