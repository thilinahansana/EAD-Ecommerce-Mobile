package com.example.shopx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class PersonalInfoFragment : Fragment() {
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRePassword: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonNext: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_persanal_info, container, false)

        editTextFirstName = view.findViewById(R.id.editTextFirstName)
        editTextLastName = view.findViewById(R.id.editTextLastName)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        editTextRePassword = view.findViewById(R.id.editTextRePassword)
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber)
        buttonNext = view.findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener {

                // Navigate to AddressDetailsFragment
                val addressDetailsFragment = AddressDetailsFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, addressDetailsFragment)
                    .addToBackStack(null)
                    .commit()

        }

        return view
    }

    fun getPersonalInfo(): PersonalInfo {
        return PersonalInfo(
            firstName = editTextFirstName.text.toString(),
            lastName = editTextLastName.text.toString(),
            email = editTextEmail.text.toString(),
            password = editTextPassword.text.toString(),
            rePassword = editTextRePassword.text.toString(),
            phoneNumber = editTextPhoneNumber.text.toString()
        )
    }
}

data class PersonalInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val rePassword: String,
    val phoneNumber: String
)