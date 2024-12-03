package com.bcs371.project3attempt2.game

import android.content.Context
import android.media.MediaPlayer
import com.bcs371.project3attempt2.R

class GameAudio(private val context: Context) {
    private var backgroundMusic: MediaPlayer? = null
    private var soundEffects: MediaPlayer? = null
    private var lastPlayedMode: Boolean? = null

    fun startBackgroundMusic(isHardMode: Boolean) {
        println("Starting background music: ${if (isHardMode) "hard" else "easy"}")
        stopBackgroundMusic()
        val musicRes = if (isHardMode) {
            R.raw.hard
        } else {
            R.raw.easy
        }
        
        lastPlayedMode = isHardMode
        backgroundMusic = MediaPlayer.create(context, musicRes).apply {
            isLooping = true
            start()
        }
    }

    fun stopBackgroundMusic() {
        println("Stopping background music")
        backgroundMusic?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        backgroundMusic = null
    }

    fun playLoseSound() {
        println("Playing lose sound")
        stopBackgroundMusic()
        soundEffects?.release()
        soundEffects = MediaPlayer.create(context, R.raw.lose).apply {
            setOnCompletionListener { 
                println("Lose sound completed, restarting background music")
                release()
                soundEffects = null
                //How i made it restart
                lastPlayedMode?.let { startBackgroundMusic(it) }
            }
            start()
        }
    }

    fun release() {
        println("Releasing all audio resources")
        stopBackgroundMusic()
        soundEffects?.release()
        soundEffects = null
        lastPlayedMode = null
    }
} 