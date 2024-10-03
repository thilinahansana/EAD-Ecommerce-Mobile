package com.example.shopx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shopx.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    // Declare view binding object
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize view binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener to navigate to OrderActivity
        binding.myOrdersContainer.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            startActivity(intent)
        }

        binding.myInfoContainer.setOnClickListener{
            val intent = Intent(activity, ProfileInformationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding object
        _binding = null
    }
}
