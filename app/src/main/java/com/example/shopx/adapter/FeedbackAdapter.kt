package com.example.shopx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.R
import com.example.shopx.model.FeedbackResponse
import java.text.SimpleDateFormat
import java.util.*

class FeedbackAdapter(
    private val feedbackList: List<FeedbackResponse>,
    private val currentUserId: String,
    private val onEditClick: (FeedbackResponse) -> Unit
) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarFeedback)
        val comment: TextView = view.findViewById(R.id.tvFeedbackComment)
        val date: TextView = view.findViewById(R.id.tvFeedbackDate)
        val editButton: Button = view.findViewById(R.id.btnEditFeedback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.ratingBar.rating = feedback.rating.toFloat()
        holder.comment.text = feedback.comment
        holder.date.text = formatDate(feedback.createdDate)

        if (feedback.customerId == currentUserId) {
            holder.editButton.visibility = View.VISIBLE
            holder.editButton.setOnClickListener { onEditClick(feedback) }
        } else {
            holder.editButton.visibility = View.GONE
        }
    }

    override fun getItemCount() = feedbackList.size

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date ?: Date())
    }
}