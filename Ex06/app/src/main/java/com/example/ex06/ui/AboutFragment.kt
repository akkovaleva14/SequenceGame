package com.example.ex06.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ex06.managers.PreferencesManager
import com.example.ex06.R
import com.example.ex06.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        val topScoreText = getString(R.string.top_score_text, preferencesManager.topScore)
        val gameConditionsText = getString(R.string.game_conditions)
        val authorText = getString(R.string.author)

        binding.recordTextView.text = topScoreText
        binding.gameConditionsTextView.text = gameConditionsText
        binding.authorTextView.text = authorText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
