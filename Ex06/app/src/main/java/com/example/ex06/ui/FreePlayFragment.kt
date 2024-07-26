package com.example.ex06.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ex06.databinding.FragmentFreePlayBinding
import androidx.lifecycle.lifecycleScope
import com.example.ex06.managers.FreePlayManager
import com.example.ex06.managers.PreferencesManager

class FreePlayFragment : Fragment() {

    private var _binding: FragmentFreePlayBinding? = null
    private val binding get() = _binding!!

    private lateinit var freePlayManager: FreePlayManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreePlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferencesManager = PreferencesManager(requireContext())
        val buttons = arrayOf(binding.button1, binding.button2, binding.button3, binding.button4)
        freePlayManager =
            FreePlayManager(requireContext(), buttons, preferencesManager, lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        freePlayManager.releaseResources()
    }
}