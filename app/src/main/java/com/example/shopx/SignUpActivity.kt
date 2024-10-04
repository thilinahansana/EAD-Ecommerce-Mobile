package com.example.shopx

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.shopx.databinding.ActivitySignupBinding
import com.example.shopx.model.Address
import com.example.shopx.model.PersonalInfo
import com.example.shopx.model.SignUpRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActivitySignup : AppCompatActivity() {
    private lateinit var personalInfoFragment: PersonalInfoFragment
    private lateinit var addressDetailsFragment: AddressDetailsFragment
    private lateinit var binding: ActivitySignupBinding
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            personalInfoFragment = PersonalInfoFragment()
            addressDetailsFragment = AddressDetailsFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, personalInfoFragment)
                .commit()
        } else {
            personalInfoFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? PersonalInfoFragment
                ?: PersonalInfoFragment()
            addressDetailsFragment = AddressDetailsFragment()
        }

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    personalInfoFragment.setImageUri(uri)
                }
            }
        }

        binding.textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save any necessary state
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
            updatedDate = formattedDate,
            profilePicture = ""
//            personalInfo.profileImageUrl
        )
    }

    private fun sendSignUpRequest(signUpRequest: SignUpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val jsonAdapter = moshi.adapter(SignUpRequest::class.java)
                val json = jsonAdapter.toJson(signUpRequest)

                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url("http://[2402:d000:a400:7088:5c68:8db9:9a1c:7c0b]/api/v1/signup")
                    .post(requestBody)
                    .build()

                val response: Response = client.newCall(request).execute()
                Log.i("SignUp", "Response: ${response}")
                handleResponse(response)
            } catch (e: Exception) {
                Log.e("SignUp", "Exception: ${e.toString()}")
            }
        }
    }

    private suspend fun handleResponse(response: Response) {
        Log.i("SignUp", "handleResponse: ${response.isSuccessful}")
        withContext(Dispatchers.Main) {
            if (response.code == 201) {
                Log.i("SignUp", "Sign Up Successful")
                Toast.makeText(this@ActivitySignup, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                // Navigate to LoginActivity
                startActivity(Intent(this@ActivitySignup, LoginActivity::class.java))
                finish() // Optionally finish the signup activity
            } else {
                Log.i("SignUp", "Sign Up Failed")
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

    fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        getImageLauncher.launch(intent)
    }

    fun getImageBase64(uri: Uri): String? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                ByteArrayOutputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                }
            }
        } catch (e: Exception) {
            Log.e("ActivitySignup", "Error converting image to Base64: ${e.message}")
            null
        }
    }
}
