package com.example.ex06.managers

import android.content.Context
import android.widget.Button
import androidx.lifecycle.LifecycleCoroutineScope

class FreePlayManager(
    context: Context,
    buttons: Array<Button>,
    preferencesManager: PreferencesManager,
    lifecycleScope: LifecycleCoroutineScope
) : BaseManager(context, lifecycleScope, buttons, preferencesManager) {

    init {
        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                playSound(index)
            }
        }
    }
}