package com.bcs371.project3attempt2.data

data class Progress(
    val userId: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val attempts: Int,
    val isHardMode: Boolean
) 