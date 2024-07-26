package com.example.ex06.managers

import android.content.Context
import android.content.res.ColorStateList
import android.widget.Button
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import androidx.core.content.ContextCompat
import com.example.ex06.R
import kotlinx.coroutines.launch

class NewGameManager(
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    private val onLevelChange: (Int) -> Unit,
    private val onTopScoreChange: (String) -> Unit,
    private val onGameOver: () -> Unit,
    buttons: Array<Button>
) : BaseManager(context, lifecycleScope, buttons, PreferencesManager(context)) {

    var sequence = mutableListOf((0..3).random())
    var userSequence = mutableListOf<Int>()
    var level = 1
    var curIndex = 0

    suspend fun playSequence() {
        lifecycleScope.launch {
            val customDelay = preferencesManager.delay
            disableButtons()
            delay(1000)
            for (index in sequence) {
                activateButton(index)
                delay(1000)
                resetColors()
                if (index != sequence.size - 1) delay(customDelay)
            }
            enableButtons()
        }
    }

    private fun activateButton(index: Int) {
        if (!lightFlag) {
            when (index) {
                0 -> buttons[0].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.colorButton1
                    )
                )

                1 -> buttons[1].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.colorButton2
                    )
                )

                2 -> buttons[2].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.colorButton3
                    )
                )

                3 -> buttons[3].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.colorButton4
                    )
                )
            }
        }
        playSound(index)
    }

    private fun playSoundHighlightButton(index: Int) {
        playSound(index)
        val button = buttons[index]
        if (lightFlag) {
            button.isPressed = true
            lifecycleScope.launch {
                delay(500)
                button.isPressed = false
                delay(500)
            }
        } else {
            lifecycleScope.launch {
                delay(1000)
            }
        }
    }

    private fun resetColors() {
        buttons.forEachIndexed { index, button ->
            button.backgroundTintList = ContextCompat.getColorStateList(
                context,
                if (lightFlag) getButtonColorStateListResourceId(index) else getButtonSelectorResourceId(
                    index
                )
            )!!
        }
    }

    fun getTopScore(): String {
        return preferencesManager.topScore!!
    }

    fun onButtonClick(index: Int) {
        lifecycleScope.launch {
            userSequence.add(index)
            playSoundHighlightButton(index)
            if (!check(curIndex++)) {
                disableButtons()
                failureSound.start()
                updateTopScore()
                onGameOver()
            } else if (curIndex == level) {
                levelComplete()
            }
        }
    }

    private fun check(curIndex: Int) = sequence[curIndex] == userSequence[curIndex]

    private fun levelComplete() {
        curIndex = 0
        userSequence.clear()
        levelUp()
        lifecycleScope.launch {
            delay(500)
            playSequence()
        }
    }

    private fun levelUp() {
        updateTopScore()
        level++
        onLevelChange(level)
        sequence.add((0..3).random())
    }

    private fun updateTopScore() {
        val topScore = preferencesManager.topScore?.toInt() ?: 1
        if (level > topScore) {
            preferencesManager.topScore = level.toString()
        }
        onTopScoreChange(preferencesManager.topScore ?: "1")
    }

    private fun disableButtons() {
        buttons.forEach { it.isEnabled = false }
    }

    private fun enableButtons() {
        buttons.forEach { it.isEnabled = true }
    }

    override fun releaseResources() {
        buttonSounds.forEach { mediaPlayer ->
            mediaPlayer.release()
        }
        failureSound.release()
    }

}
