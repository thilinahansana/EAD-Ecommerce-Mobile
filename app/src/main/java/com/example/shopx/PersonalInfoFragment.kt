package com.example.shopx

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopx.model.PersonalInfo
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

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

    private var imageUri: Uri? = null
    private var profileImageUrl: String? = null

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
                if (imageUri != null) {
                    uploadImageToFirebase()
                } else {
                    proceedToNextStep()
                }
            }
        }

        return view
    }

    fun setImageUri(uri: Uri) {
        imageUri = uri
        imageViewProfile.setImageURI(uri)
    }

    private fun uploadImageToFirebase() {
        val fileName = UUID.randomUUID().toString()
        val refStorage = FirebaseStorage.getInstance().reference.child("profile_images/$fileName")

        buttonNext.isEnabled = false // Disable button to prevent multiple clicks

        refStorage.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                refStorage.downloadUrl.addOnSuccessListener { uri ->
                    profileImageUrl = uri.toString()
                    proceedToNextStep()
                }.addOnFailureListener { e ->
                    handleError("Failed to get download URL: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                handleError("Failed to upload image: ${e.message}")
            }
    }

    private fun proceedToNextStep() {
        buttonNext.isEnabled = true // Re-enable button
        (activity as? ActivitySignup)?.navigateToAddressDetails()
    }

    private fun handleError(errorMessage: String) {
        buttonNext.isEnabled = true // Re-enable button
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    fun getPersonalInfo(): PersonalInfo {
        return PersonalInfo(
            firstName = editTextFirstName.text.toString(),
            lastName = editTextLastName.text.toString(),
            email = editTextEmail.text.toString(),
            password = editTextPassword.text.toString(),
            rePassword = editTextRePassword.text.toString(),
            phoneNumber = editTextPhoneNumber.text.toString(),
            profileImageUrl = profileImageUrl
        )
    }

    private fun validateInputs(): Boolean {
        // Add your validation logic here
        // For example:
        if (editTextFirstName.text.isNullOrBlank()) {
            Toast.makeText(context, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return false
        }
        // Add similar checks for other fields
        return true
    }
}