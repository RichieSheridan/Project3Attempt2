package com.bcs371.project3attempt2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): User?
    
    @Insert
    suspend fun insertUser(user: User)
    
    @Insert
    suspend fun insertGameProgress(progress: GameProgress)
    
    @Query("SELECT * FROM game_progress WHERE childId = :childId")
    fun getGameProgressForChild(childId: Int): Flow<List<GameProgress>>
    
    @Insert
    suspend fun insertAttempt(attempt: GameAttempt)
    
    @Query("""
        SELECT * FROM game_attempts 
        WHERE childId = :userId AND isHardMode = :isHardMode 
        ORDER BY timestamp DESC 
        LIMIT :limit
    """)
    fun getRecentAttempts(userId: Int, isHardMode: Boolean, limit: Int = 10): Flow<List<GameAttempt>>
} 