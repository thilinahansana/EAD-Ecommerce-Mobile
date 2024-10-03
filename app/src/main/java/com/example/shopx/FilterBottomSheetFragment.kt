package com.example.shopx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.shopx.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment(private val onApplyFilter: (String, String, Double?, Double?) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for the Apply Filter button
        binding.btnApplyFilter.setOnClickListener {
            val typeRadioButtonId = binding.radioGroupType.checkedRadioButtonId
            val typeRadioButton = view.findViewById<RadioButton>(typeRadioButtonId)
            val type = typeRadioButton?.text?.toString() ?: "All"

            val sizeRadioButtonId = binding.radioGroupSize.checkedRadioButtonId
            val sizeRadioButton = view.findViewById<RadioButton>(sizeRadioButtonId)
            val size = sizeRadioButton?.text?.toString() ?: "All"

            val minPriceText = binding.editTextMinPrice.text.toString()
            val maxPriceText = binding.editTextMaxPrice.text.toString()
            val minPrice = minPriceText.toDoubleOrNull()
            val maxPrice = maxPriceText.toDoubleOrNull()

            onApplyFilter(type, size, minPrice, maxPrice)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

