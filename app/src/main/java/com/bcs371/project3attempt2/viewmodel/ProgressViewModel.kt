package com.bcs371.project3attempt2.viewmodel

import androidx.lifecycle.ViewModel
import com.bcs371.project3attempt2.data.ProgressDao
import com.bcs371.project3attempt2.data.Progress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressViewModel(private val progressDao: ProgressDao) : ViewModel() {
    private val _progress = MutableStateFlow<List<Progress>>(emptyList())
    val progress: StateFlow<List<Progress>> = _progress.asStateFlow()

    fun addProgress(userId: Int, attempts: Int, isHardMode: Boolean) {
        val newProgress = Progress(userId, attempts = attempts, isHardMode = isHardMode)
        _progress.update { currentList ->
            currentList + newProgress
        }
    }

    fun getProgressForUser(userId: Int): List<Progress> {
        return progress.value.filter { it.userId == userId }
    }
} 