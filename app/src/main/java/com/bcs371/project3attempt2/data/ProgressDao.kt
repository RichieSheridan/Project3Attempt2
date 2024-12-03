package com.bcs371.project3attempt2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProgressDao {
    @Insert
    suspend fun insert(progress: ProgressEntity)

    @Query("SELECT * FROM progress_table WHERE userId = :userId")
    suspend fun getProgressForUser(userId: Int): List<ProgressEntity>

    @Query("SELECT * FROM progress_table")
    suspend fun getAllProgress(): List<ProgressEntity>
} 