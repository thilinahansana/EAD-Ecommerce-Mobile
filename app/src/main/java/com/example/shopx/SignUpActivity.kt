package com.example.shopx

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val re_Password: String,
    val phoneNumber: String,
    val addresss: Address,
    val role: Int = 2,
    val createdDate: String,
    val updatedDate: String,
    val isActive: Boolean = true,
    val profilePicture: String = "string"
)

@JsonClass(generateAdapter = true)
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String
)

class ActivitySignup : AppCompatActivity() {
    private lateinit var personalInfoFragment: PersonalInfoFragment
    private lateinit var addressDetailsFragment: AddressDetailsFragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        personalInfoFragment = PersonalInfoFragment()
        addressDetailsFragment = AddressDetailsFragment()

        // Start with the PersonalInfoFragment
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, personalInfoFragment)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddressDetailsComplete(address: Address) {
        val personalInfo = personalInfoFragment.getPersonalInfo()
        val signUpRequest = createSignUpRequest(personalInfo, address)
        sendSignUpRequest(signUpRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSignUpRequest(personalInfo: PersonalInfo, address: Address): SignUpRequest {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formattedDate = currentDateTime.format(formatter)

        return SignUpRequest(
            firstName = personalInfo.firstName,
            lastName = personalInfo.lastName,
            email = personalInfo.email,
            password = personalInfo.password,
            re_Password = personalInfo.rePassword,
            phoneNumber = personalInfo.phoneNumber,
            addresss = address,
            role = 2,
            createdDate = formattedDate,
            updatedDate = formattedDate
        )
    }

    private fun sendSignUpRequest(signUpRequest: SignUpRequest) {
        val client = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(SignUpRequest::class.java)
        val json = jsonAdapter.toJson(signUpRequest)
        Log.d("SignUp", "Request JSON: $json") // Log the JSON request

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("http://[2402:d000:a400:fd5b:f047:a488:ec99:5e1]/api/v1/signup")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response = client.newCall(request).execute()
                handleResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SignUp", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ActivitySignup, "Sign Up Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun handleResponse(response: Response) {
        withContext(Dispatchers.Main) {
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                Log.d("SignUp", "Successful response: $responseBody")
                Toast.makeText(this@ActivitySignup, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                // TODO: Navigate to the next screen or login screen
            } else {
                Log.e("SignUp", "Error response: $responseBody")
                Log.e("SignUp", "Response code: ${response.code}")
                Log.e("SignUp", "Response message: ${response.message}")
                Toast.makeText(this@ActivitySignup, "Sign Up Failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun navigateToAddressDetails() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addressDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}