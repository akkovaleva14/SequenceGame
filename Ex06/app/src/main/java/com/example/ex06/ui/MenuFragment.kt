package com.example.ex06.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ex06.R
import com.example.ex06.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_newGameFragment)
        }
        binding.buttonFreePlay.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_freePlayFragment)
        }
        binding.buttonAbout.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_aboutFragment)
        }
        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}