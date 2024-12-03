package com.bcs371.project3attempt2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bcs371.project3attempt2.data.ProgressDao

class ProgressViewModelFactory(private val progressDao: ProgressDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgressViewModel(progressDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 