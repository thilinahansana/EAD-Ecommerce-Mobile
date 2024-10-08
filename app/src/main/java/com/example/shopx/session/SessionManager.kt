package com.example.shopx.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.shopx.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    //save user session
    fun saveUserSession(token: String, userJson: String) {
        Log.d("SessionManager", "Saving user JSON: $userJson")
        prefs.edit()
            .putBoolean("IS_LOGGED_IN", true)
            .putString("JWT_TOKEN", token)
            .putString("USER_JSON", userJson)
            .apply()
    }

//    get user token
    fun getToken(): String? = prefs.getString("JWT_TOKEN", null)

//     get user details
    fun getUser(): User? {
        val userJson = prefs.getString("USER_JSON", null) ?: return null
        Log.d("SessionManager", "User JSON retrieved: $userJson")
        return try {
            moshi.adapter(User::class.java).fromJson(userJson)
        } catch (e: Exception) {
            Log.e("SessionManager", "Failed to parse user JSON", e)
            null
        }
    }

    // check is user loggedin
    fun isUserLoggedIn(): Boolean = prefs.getBoolean("IS_LOGGED_IN", false)

    // clear use session (logout)
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // decode user token
    fun decodeToken(): Map<String, Any>? {
        val token = getToken() ?: return null
        return try {
            val jwt = JWT(token)
            jwt.claims.mapValues { it.value.asString() ?: it.value.asInt() ?: it.value.asDouble() ?: it.value.asLong() ?: it.value.asBoolean() ?: it.value.asList(String::class.java) }
        } catch (e: Exception) {
            Log.e("SessionManager", "Failed to decode token", e)
            null
        }
    }
}
