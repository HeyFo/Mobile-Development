package com.bangkit.heyfo.ui.detail.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.heyfo.databinding.FragmentStepsBinding

class StepsFragment : Fragment() {
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!

    private var steps: List<String> = emptyList()
    private var isViewCreated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isViewCreated = false
    }

    fun setSteps(newSteps: List<String>) {
        steps = newSteps
        updateUI()
    }

    private fun updateUI() {
        if (isViewCreated) {
            binding.textViewSteps.text = steps.joinToString(separator = "\n", prefix = "\n• ")
        }
    }
}
