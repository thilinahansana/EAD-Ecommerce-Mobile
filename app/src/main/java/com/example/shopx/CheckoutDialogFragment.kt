package com.example.shopx

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

@SuppressLint("MissingInflatedId")
class CheckoutDialog(context: Context, private val onCheckout: (String, String) -> Unit) : Dialog(context) {

    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnPlaceOrder: Button

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_checkout_dialog, null)
        setContentView(view)
        setTitle("Checkout")

        etAddress = view.findViewById(R.id.etAddress)
        etPhone = view.findViewById(R.id.etPhone)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)

        btnPlaceOrder.setOnClickListener {
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()

            if (address.isNotEmpty() && phone.isNotEmpty()) {
                onCheckout(address, phone)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter both address and phone number.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
