package com.example.foodapp.user.Fragment

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
    private lateinit var binding: FragmentNotifactionBottomBinding
    private var notificationText: String? = null
    private var notificationImage: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationText = arguments?.getString(ARG_NOTIFICATION_TEXT)
        notificationImage = arguments?.getInt(ARG_NOTIFICATION_IMAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotifactionBottomBinding.inflate(inflater, container, false)

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val notifications = listOf(notificationText ?: "No notification")
        val images = listOf(notificationImage ?: R.drawable.ic_logo)

        val adapter = NotificationAdapter(ArrayList(notifications), ArrayList(images))
        binding.notificationRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {
        private const val ARG_NOTIFICATION_TEXT = "notification_text"
        private const val ARG_NOTIFICATION_IMAGE = "notification_image"

        fun newInstance(notificationText: String, notificationImage: Int): Notifaction_Bottom_Fragment {
            val fragment = Notifaction_Bottom_Fragment()
            val args = Bundle().apply {
                putString(ARG_NOTIFICATION_TEXT, notificationText)
                putInt(ARG_NOTIFICATION_IMAGE, notificationImage)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
