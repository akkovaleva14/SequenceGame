package com.example.ex06.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ex06.managers.PreferencesManager
import com.example.ex06.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())

        binding.soundSwitch.isChecked = !preferencesManager.isSoundEnabled
        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.isSoundEnabled = !isChecked
        }

        binding.sliderDelay.value = preferencesManager.delay.toFloat()
        binding.sliderDelay.addOnChangeListener { _, value, _ ->
            preferencesManager.delay = value.toLong()
        }

        binding.highlightSwitch.isChecked = preferencesManager.isLightEnabled
        binding.highlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.isLightEnabled = isChecked
        }

        binding.themeSpinner.setSelection(preferencesManager.theme)
        binding.themeSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        preferencesManager.theme = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
