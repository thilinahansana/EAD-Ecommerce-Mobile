package com.example.shopx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopx.databinding.FragmentProfileBinding
import com.example.shopx.session.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        displayUserInfo()

        binding.myOrdersContainer.setOnClickListener {
            startActivity(Intent(activity, OrderActivity::class.java))
        }

        binding.myInfoContainer.setOnClickListener {
            startActivity(Intent(activity, ProfileInformationActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()
            navigateToLogin()
        }
    }

    private fun displayUserInfo() {
        val user = sessionManager.getUser()
        user?.let {
            binding.userName.text = "${it.firstName} ${it.lastName}"
            binding.userEmail.text = it.email

            Glide.with(this)
                .load(it.profilePicture).apply(RequestOptions.circleCropTransform())
                .into(binding.profileImage)
            // Add more fields as needed
        } ?: run {
            // Handle the case where user is null (maybe show a default message)
            binding.userName.text = "Guest"
            binding.userEmail.text = "No email available"
        }

        // Display some information from the JWT if needed
//        sessionManager.decodeToken()?.let { claims ->
//            // Example: Display the role from JWT
//            binding.textViewRole.text = "Role: ${claims["role"]}"
//        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

