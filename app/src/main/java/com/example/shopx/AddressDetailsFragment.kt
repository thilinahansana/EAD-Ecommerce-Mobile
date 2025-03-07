package com.example.shopx

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.shopx.model.Address

class AddressDetailsFragment : Fragment() {
    private lateinit var editTextAddressStreet: EditText
    private lateinit var editTextAddressCity: EditText
    private lateinit var editTextAddressState: EditText
    private lateinit var editTextAddressCountry: EditText
    private lateinit var editTextAddressZipCode: EditText
    private lateinit var buttonSignUp: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_address_details, container, false)

        editTextAddressStreet = view.findViewById(R.id.editTextAddressStreet)
        editTextAddressCity = view.findViewById(R.id.editTextAddressCity)
        editTextAddressState = view.findViewById(R.id.editTextAddressState)
        editTextAddressCountry = view.findViewById(R.id.editTextAddressCountry)
        editTextAddressZipCode = view.findViewById(R.id.editTextAddressZipCode)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)

        buttonSignUp.setOnClickListener {
            if (validateInputs()) {
                (activity as? ActivitySignup)?.onAddressDetailsComplete(getAddressDetails())
            }
        }

        return view
    }

    private fun getAddressDetails(): Address {
        return Address(
            street = editTextAddressStreet.text.toString(),
            city = editTextAddressCity.text.toString(),
            state = editTextAddressState.text.toString(),
            country = editTextAddressCountry.text.toString(),
            zipCode = editTextAddressZipCode.text.toString()
        )
    }

    private fun validateInputs(): Boolean {
        // Add your validation logic here
        // Return true if all inputs are valid, false otherwise
        return true
    }
}