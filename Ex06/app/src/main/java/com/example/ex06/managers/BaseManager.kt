package com.example.ex06.managers

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.ex06.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseManager(
    protected val context: Context,
    protected val lifecycleScope: LifecycleCoroutineScope,
    protected val buttons: Array<Button>,
    protected val preferencesManager: PreferencesManager
) {
    lateinit var buttonSounds: Array<MediaPlayer>
    protected lateinit var failureSound: MediaPlayer
    var lightFlag = false

    init {
        init()
    }

    fun init() {
        soundInit(Themes.entries[preferencesManager.theme])
        setupVolume()
        lightFlag = preferencesManager.isLightEnabled
        setupStateList()
    }

    private fun soundInit(theme: Themes) {
        val soundIds = getSoundIds(theme)
        buttonSounds =
            soundIds.map { soundId -> MediaPlayer.create(context, soundId) }.toTypedArray()
        failureSound = MediaPlayer.create(context, R.raw.failure)
    }

    private fun setupVolume() {
        val volume = if (preferencesManager.isSoundEnabled) 1f else 0f
        buttonSounds.forEach { it.setVolume(volume, volume) }
        failureSound.setVolume(volume, volume)
    }

    private fun setupStateList() {
        if (lightFlag) {
            buttons.forEachIndexed { index, button ->
                button.backgroundTintList = ContextCompat.getColorStateList(
                    context,
                    getButtonColorStateListResourceId(index)
                )!!
            }
        } else {
            buttons.forEachIndexed { index, button ->
                button.backgroundTintList = ContextCompat.getColorStateList(
                    context,
                    getButtonSelectorResourceId(index)
                )!!
            }
        }
    }

    private fun getSoundIds(theme: Themes): Array<Int> {
        return when (theme) {
            Themes.ANIMALS -> arrayOf(R.raw.bird, R.raw.cat, R.raw.dog, R.raw.rooster)
            Themes.FUN -> arrayOf(R.raw.clown, R.raw.laugh, R.raw.fart, R.raw.giggle)
            Themes.HUMAN -> arrayOf(R.raw.exclamation, R.raw.roar, R.raw.scream, R.raw.sneeze)
        }
    }

    fun getButtonColorStateListResourceId(index: Int): Int {
        return when (index) {
            0 -> R.color.button1_selector_no_highlight
            1 -> R.color.button2_selector_no_highlight
            2 -> R.color.button3_selector_no_highlight
            3 -> R.color.button4_selector_no_highlight
            else -> throw IllegalArgumentException("Invalid button index")
        }
    }

    fun getButtonSelectorResourceId(index: Int): Int {
        return when (index) {
            0 -> R.color.button1_selector
            1 -> R.color.button2_selector
            2 -> R.color.button3_selector
            3 -> R.color.button4_selector
            else -> throw IllegalArgumentException("Invalid button index")
        }
    }

    protected fun playSound(index: Int) {
        val mediaPlayer = buttonSounds[index]
        lifecycleScope.launch {
            try {
                mediaPlayer.start()
                delay(1000)
                withContext(Dispatchers.IO) {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.prepare()
                    }
                }
            } catch (e: Exception) {
                Log.e("playSound", "Error playing sound", e)
            }
        }
    }

    open fun releaseResources() {
        buttonSounds.forEach { mediaPlayer ->
            mediaPlayer.release()
        }
    }

}
