package com.example.ex06.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

enum class Themes { ANIMALS, FUN, HUMAN }

class PreferencesManager(context: Context) {
    private val pref: SharedPreferences =
        context.getSharedPreferences("Storage", AppCompatActivity.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    var topScore: String?
        get() = pref.getString(KEY_TOP_SCORE, "1")
        set(value) {
            editor.putString(KEY_TOP_SCORE, value).apply()
        }

    var isSoundEnabled: Boolean
        get() = pref.getBoolean(KEY_SOUND, true)
        set(value) {
            editor.putBoolean(KEY_SOUND, value).apply()
        }

    var delay: Long
        get() = pref.getLong(KEY_DELAY, 100)
        set(value) {
            editor.putLong(KEY_DELAY, value).apply()
        }

    var isLightEnabled: Boolean
        get() = pref.getBoolean(KEY_LIGHT, false)
        set(value) {
            editor.putBoolean(KEY_LIGHT, value).apply()
        }

    var theme: Int
        get() = pref.getInt(KEY_THEME, 0)
        set(value) {
            editor.putInt(KEY_THEME, value).apply()
        }

    companion object {
        private const val KEY_TOP_SCORE = "topScore"
        private const val KEY_SOUND = "mute"
        private const val KEY_DELAY = "delay"
        private const val KEY_LIGHT = "light"
        private const val KEY_THEME = "theme"
    }
}
