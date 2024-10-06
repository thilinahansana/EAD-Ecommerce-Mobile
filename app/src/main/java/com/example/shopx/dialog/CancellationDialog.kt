package com.example.shopx.dialog

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.example.shopx.R

class CancellationDialog(context: Context, private val onNoteEntered: (String) -> Unit) {
    private val dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.dialog_cancellation)
        dialog.setCancelable(true)

        val cancelButton = dialog.findViewById<Button>(R.id.cancel_button)
        val confirmButton = dialog.findViewById<Button>(R.id.confirm_button)
        val noteEditText = dialog.findViewById<EditText>(R.id.note_edit_text)

        confirmButton.setOnClickListener {
            val note = noteEditText.text.toString().trim()
            if (note.isNotEmpty()) {
                onNoteEntered(note)
                dialog.dismiss()
            } else {
                // Handle empty note (optional)
                noteEditText.error = "Note cannot be empty"
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}
