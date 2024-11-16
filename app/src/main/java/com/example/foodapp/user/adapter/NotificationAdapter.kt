package com.example.foodapp.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.NotificationItemBinding

class NotificationAdapter(private val notification : ArrayList<String>, private val notificationImage: ArrayList<Int>) : RecyclerView.Adapter<NotificationAdapter.NotificatitonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificatitonViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificatitonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificatitonViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = notification.size

    inner class NotificatitonViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                notificationTextView.text = notification[position]
                notificationImageView.setImageResource(notificationImage[position])
            }
        }

    }
}