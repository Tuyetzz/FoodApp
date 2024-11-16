package com.example.foodapp.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.databinding.FragmentNotifactionBottomBinding
import com.example.foodapp.user.adapter.NotificationAdapter
import com.example.foodapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notifaction_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentNotifactionBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifactionBottomBinding.inflate(layoutInflater, container, false)
        val notifications = listOf("Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed")
        val notificationImages = listOf(R.drawable.sademoji, R.drawable.ship, R.drawable.illustration)
        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}