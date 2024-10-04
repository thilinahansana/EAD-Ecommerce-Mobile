package com.example.shopx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopx.databinding.ActivityLoginBinding
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val sessionManager: SessionManager by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val loginRequest = createLoginRequest()
            if (loginRequest != null) {
                sendLoginRequest(loginRequest)
            }
        }
        binding.textViewSignUp.setOnClickListener {
            val intent = Intent(this, ActivitySignup::class.java)
            startActivity(intent)
        }

    }

    private fun createLoginRequest(): LoginRequest? {
        return LoginRequest(
            email = binding.editTextEmail.text.toString(),
            password = binding.editTextPassword.text.toString()
        )
    }

    private fun sendLoginRequest(loginRequest: LoginRequest) {
        val client = OkHttpClient()
        val moshi = Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(LoginRequest::class.java)
        val json = jsonAdapter.toJson(loginRequest)

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("http://[2402:d000:a400:7088:5c68:8db9:9a1c:7c0b]/api/v1/login")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response = client.newCall(request).execute()
                handleResponse(response)
            } catch (e: Exception) {
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
            if (response.code == 200) {
                Log.d("Login", "Successful response: $responseBody")
                val jsonObject = JSONObject(responseBody)
                val token = jsonObject.getString("token")
                val userJson = jsonObject.getJSONObject("user").toString()

                sessionManager.saveUserSession(token, userJson)

                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Log.e("Login", "Error response: $responseBody")
                Toast.makeText(this@LoginActivity, "Login Failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}