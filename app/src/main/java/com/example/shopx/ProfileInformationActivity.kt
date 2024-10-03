package com.example.shopx


import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileInformationActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)

        // Initialize views
        nameInput = findViewById(R.id.name_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)

        // Handle the Update Profile button click
        findViewById<Button>(R.id.update_profile_button).setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        // Fetch the user inputs
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // This is where you'd usually send the updated data to your server or database
        // Simulating a profile update with a Toast message for now
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // Example: Send data to server or database
        // updateUserProfile(name, email, phone)
    }

    // Simulated function to update profile in database or server
    private fun updateUserProfile(name: String, email: String, phone: String) {
        // Implement your API call or database update logic here
    }
}
