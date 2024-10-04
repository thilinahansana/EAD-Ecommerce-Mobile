package com.example.shopx

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileInformationActivity : AppCompatActivity() {

    private lateinit var firstnameInput: TextInputEditText
    private lateinit var lastnameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var profileImageView: ImageView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)

        // Initialize views
        firstnameInput = findViewById(R.id.first_name_input)
        lastnameInput = findViewById(R.id.last_name_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)
        profileImageView = findViewById(R.id.profile_image)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Display user information
        displayUserInfo()

        // Handle the Update Profile button click
        findViewById<Button>(R.id.update_profile_button).setOnClickListener {
            updateProfile()
        }
    }

    private fun displayUserInfo() {
        val user = sessionManager.getUser()
        user?.let {
            firstnameInput.setText(it.firstName)
            lastnameInput.setText(it.lastName)
            emailInput.setText(it.email)
            phoneInput.setText(it.phoneNumber ?: "")

            // Load profile picture if available
            if (!it.profilePicture.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.profilePicture)
                    .into(profileImageView)
            }
        }
    }

    private fun updateProfile() {
        // Fetch the user inputs
        val fname = firstnameInput.text.toString()
        val lname = lastnameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()

        // Validate inputs
        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulating a profile update with a Toast message for now
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // Example: Send data to server or database
        // updateUserProfile(fname, lname, email, phone)
    }

    // Simulated function to update profile in database or server
    private fun updateUserProfile(fname: String, lname: String, email: String, phone: String) {
        // Implement your API call or database update logic here
    }
}
