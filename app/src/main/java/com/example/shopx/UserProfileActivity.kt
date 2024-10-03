package com.example.shopx


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar

class UserProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_persanal_info)

        // Set up user details (you can fetch from a server)
        val userName: TextView = findViewById(R.id.user_name)
        val userEmail: TextView = findViewById(R.id.user_email)
        userName.text = "John Doe"
        userEmail.text = "john.doe@example.com"

        // My Orders
        val myOrdersCard: CardView = findViewById(R.id.my_orders_card)
        myOrdersCard.setOnClickListener {
            // Navigate to Orders Activity
            Snackbar.make(it, "Navigating to My Orders...", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        // Logout
        val logoutButton: Button = findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Handle logout
            Snackbar.make(it, "Logging out...", Snackbar.LENGTH_SHORT).show()
            // Navigate back to login or splash screen
        }
    }
}
