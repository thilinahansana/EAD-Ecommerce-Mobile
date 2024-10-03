package com.example.shopx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment

class RatingDialogFragment(private val onRatingSubmitted: (Float, String) -> Unit) : DialogFragment() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_rating, container, false)

        ratingBar = view.findViewById(R.id.ratingBar)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        submitButton = view.findViewById(R.id.submitRatingButton)

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val review = reviewEditText.text.toString()
            onRatingSubmitted(rating, review)
            dismiss()
        }

        return view
    }
}
