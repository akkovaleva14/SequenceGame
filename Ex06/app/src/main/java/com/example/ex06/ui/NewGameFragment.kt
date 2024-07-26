package com.example.ex06.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ex06.databinding.FragmentNewGameBinding
import androidx.navigation.fragment.findNavController
import android.app.AlertDialog
import com.example.ex06.managers.NewGameManager
import com.example.ex06.R
import kotlinx.coroutines.*

class NewGameFragment : Fragment() {

    private var _binding: FragmentNewGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var newGameManager: NewGameManager
    private lateinit var buttons: Array<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttons = arrayOf(binding.button1, binding.button2, binding.button3, binding.button4)

        newGameManager = NewGameManager(
            context = requireContext(),
            lifecycleScope = lifecycleScope,
            onLevelChange = { level ->
                binding.levelTextView.text = getString(R.string.level_text, level)
            },
            onTopScoreChange = { topScore ->
                binding.topScoreTextView.text = getString(R.string.top_score_text, topScore)
            },
            onGameOver = { showScoreDialog() },
            buttons = buttons
        )
        newGameManager.init()

        binding.levelTextView.text = getString(R.string.level_text, 1)
        binding.topScoreTextView.text =
            getString(R.string.top_score_text, newGameManager.getTopScore())

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                newGameManager.onButtonClick(index)
            }
        }
        lifecycleScope.launch { newGameManager.playSequence() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("sequence", ArrayList(newGameManager.sequence))
        outState.putIntegerArrayList("userSequence", ArrayList(newGameManager.userSequence))
        outState.putInt("level", newGameManager.level)
        outState.putInt("curIndex", newGameManager.curIndex)
        outState.putBoolean("lightFlag", newGameManager.lightFlag)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            newGameManager.sequence =
                it.getIntegerArrayList("sequence")?.toMutableList() ?: mutableListOf()
            newGameManager.userSequence =
                it.getIntegerArrayList("userSequence")?.toMutableList() ?: mutableListOf()
            newGameManager.level = it.getInt("level", 1)
            newGameManager.curIndex = it.getInt("curIndex", 0)
            newGameManager.lightFlag = it.getBoolean("lightFlag", false)

            binding.levelTextView.text = getString(R.string.level_text, newGameManager.level)
            binding.topScoreTextView.text =
                getString(R.string.top_score_text, newGameManager.getTopScore())

            lifecycleScope.launch { newGameManager.playSequence() }
        }
    }

    private fun showScoreDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Game Over")
            .setMessage("You reached level ${newGameManager.level}")
            .setPositiveButton("Restart") { _, _ ->
                requireActivity().recreate()
            }
            .setNegativeButton("Menu") { _, _ ->
                findNavController().navigate(R.id.action_newGameFragment_to_menuFragment)
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        newGameManager.releaseResources()
    }
}