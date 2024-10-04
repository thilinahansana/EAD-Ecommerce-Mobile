package com.example.shopx

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.shopx.ActivitySignup
import com.example.shopx.R
import com.example.shopx.model.PersonalInfo

class PersonalInfoFragment : Fragment() {
    private lateinit var imageViewProfile: ImageView
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRePassword: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonNext: Button

    private var imageBase64: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_persanal_info, container, false)

        imageViewProfile = view.findViewById(R.id.imageViewProfile)
        editTextFirstName = view.findViewById(R.id.editTextFirstName)
        editTextLastName = view.findViewById(R.id.editTextLastName)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        editTextRePassword = view.findViewById(R.id.editTextRePassword)
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber)
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage)
        buttonNext = view.findViewById(R.id.buttonNext)

        buttonSelectImage.setOnClickListener {
            (activity as? ActivitySignup)?.openImagePicker()
        }

        buttonNext.setOnClickListener {
            if (validateInputs()) {
                (activity as? ActivitySignup)?.navigateToAddressDetails()
            }
        }

        return view
    }

    fun setImageUri(uri: Uri) {
        imageViewProfile.setImageURI(uri)
        imageBase64 = (activity as? ActivitySignup)?.getImageBase64(uri)
    }

    fun getPersonalInfo(): PersonalInfo {
        return PersonalInfo(
            firstName = editTextFirstName.text.toString(),
            lastName = editTextLastName.text.toString(),
            email = editTextEmail.text.toString(),
            password = editTextPassword.text.toString(),
            rePassword = editTextRePassword.text.toString(),
            phoneNumber = editTextPhoneNumber.text.toString(),
            profileImageUrl = imageBase64 // This is now the Base64 string of the image
        )
    }

    private fun validateInputs(): Boolean {
        // Add your validation logic here
        // Return true if all inputs are valid, false otherwise
        return true
    }
}