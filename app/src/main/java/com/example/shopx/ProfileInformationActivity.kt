package com.example.shopx

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopx.model.Address
import com.example.shopx.model.UpdateUserRequest
import com.google.android.material.textfield.TextInputEditText
import com.example.shopx.service.ApiService
import com.example.shopx.service.RetrofitInstance
import com.example.shopx.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class ProfileInformationActivity : AppCompatActivity() {

    private lateinit var firstnameInput: TextInputEditText
    private lateinit var lastnameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var streetInput: TextInputEditText
    private lateinit var cityInput: TextInputEditText
    private lateinit var stateInput: TextInputEditText
    private lateinit var countryInput: TextInputEditText
    private lateinit var zipInput: TextInputEditText
    private lateinit var profileImageView: ImageView
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)

        // Initialize views
        firstnameInput = findViewById(R.id.first_name_input)
        lastnameInput = findViewById(R.id.last_name_input)
        emailInput = findViewById(R.id.email_input)
        phoneInput = findViewById(R.id.phone_input)
        streetInput = findViewById(R.id.street_input)
        cityInput = findViewById(R.id.city_input)
        stateInput = findViewById(R.id.state_input)
        countryInput = findViewById(R.id.country_input)
        zipInput = findViewById(R.id.zip_input)
        profileImageView = findViewById(R.id.profile_image)

        // Initialize SessionManager and Retrofit API Service
        sessionManager = SessionManager(this)
        apiService = RetrofitInstance.api

        // Display user information
        displayUserInfo()

        // Handle the Update Profile button click
        findViewById<Button>(R.id.update_profile_button).setOnClickListener {
            updateProfile()
        }

        findViewById<Button>(R.id.deactivate_profile_button).setOnClickListener{
            deactivateAccount()
        }
    }

    private fun deactivateAccount() {
        val userId = sessionManager.getUser()?.id
        Log.d("ProfileInfo", "Deactivating account for User ID: $userId")

        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
            return
        }

        // Call the API to deactivate the account
        val call = apiService.deactivateUser(userId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("ProfileInfo", "Deactivate Response code: ${response.code()}")
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@ProfileInformationActivity, "Account deactivated successfully", Toast.LENGTH_SHORT).show()
                        // Optionally, redirect to login or main activity
                        finish() // Close the activity
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ProfileInformationActivity, "Account deactivation failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProfileInfo", "API call failed", t)
                runOnUiThread {
                    Toast.makeText(this@ProfileInformationActivity, "Failed to deactivate account: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun displayUserInfo() {
        val user = sessionManager.getUser()
        user?.let {
            firstnameInput.setText(it.firstName)
            lastnameInput.setText(it.lastName)
            emailInput.setText(it.email)
            phoneInput.setText(it.phoneNumber ?: "")
            streetInput.setText(it.addresss?.street ?: "")
            cityInput.setText(it.addresss?.city ?: "")
            stateInput.setText(it.addresss?.state ?: "")
            countryInput.setText(it.addresss?.country ?: "")
            zipInput.setText(it.addresss?.zipCode ?: "")

            // Load profile picture if available
            it.profilePicture?.let { pictureUrl ->
                if (pictureUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(pictureUrl)
                        .apply(RequestOptions.circleCropTransform()) // Updated this line
                        .into(profileImageView)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProfile() {
        // Fetch the user inputs
        val fname = firstnameInput.text.toString()
        val lname = lastnameInput.text.toString()
        val phone = phoneInput.text.toString()
        val street = streetInput.text.toString()
        val city = cityInput.text.toString()
        val state = stateInput.text.toString()
        val country = countryInput.text.toString()
        val zip = zipInput.text.toString()

        // Log input values
        Log.d("ProfileInfo", "Updating profile: $fname $lname, $phone, $street, $city, $state, $country, $zip")

        // Validate inputs
        if (listOf(fname, lname, phone, street, city, state, country, zip).any { it.isEmpty() }) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Get current date and time
        val currentDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formattedCurrentDate = currentDateTime.format(formatter)

        // Prepare data for updating the profile
        val updateData = UpdateUserRequest(
            firstName = fname,
            lastName = lname,
            phoneNumber = phone,
            role = 2,
            address = Address(
                street = street,
                city = city,
                state = state,
                country = country,
                zipCode = zip
            ),
            profilePicture = "", // Use a valid image URL or keep it empty if not updating
            updatedDate = formattedCurrentDate
        )

        // Call API to update user profile
        val userId = sessionManager.getUser()?.id
        Log.d("ProfileInfo", "User ID: $userId")

        if (userId == null) {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.updateUser(userId, updateData)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("ProfileInfo", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileInformationActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileInformationActivity, "Profile update failed: ${response.errorBody()?.string() ?: response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProfileInfo", "API call failed", t)
                Toast.makeText(this@ProfileInformationActivity, "Failed to update profile: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })



    }
}
