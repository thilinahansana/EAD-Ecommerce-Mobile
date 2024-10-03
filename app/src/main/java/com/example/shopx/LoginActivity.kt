package com.example.shopx

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)  // Ensure this matches your XML filename

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val loginRequest = createLoginRequest()
            if (loginRequest != null) {
                sendLoginRequest(loginRequest)
            }
        }
    }

    private fun createLoginRequest(): LoginRequest? {
        // Get input values and create LoginRequest object
        return LoginRequest(
            email = editTextEmail.text.toString(),
            password = editTextPassword.text.toString()
        )
    }

    private fun sendLoginRequest(loginRequest: LoginRequest) {
        val client = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(LoginRequest::class.java)
        val json = jsonAdapter.toJson(loginRequest)
        Log.d("Login", "Request JSON: $json") // Log the JSON request

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("http://[2402:d000:a400:fd5b:f047:a488:ec99:5e1]/api/v1/login")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response = client.newCall(request).execute()
                handleResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Login", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun handleResponse(response: Response) {
        withContext(Dispatchers.Main) {
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                Log.d("Login", "Successful response: $responseBody")
                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                // Handle successful login (e.g., navigate to another activity)
            } else {
                Log.e("Login", "Error response: $responseBody")
                Log.e("Login", "Response code: ${response.code}")
                Log.e("Login", "Response message: ${response.message}")
                Toast.makeText(this@LoginActivity, "Login Failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}