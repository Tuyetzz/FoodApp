package com.example.foodapp.user.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.databinding.FragmentCongratsBottomSheetBinding
import com.example.foodapp.user.model.UserModel
import com.example.foodapp.user.view.MainUserActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCongratsBottomSheetBinding
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lấy `user` và các flag từ arguments
        user = arguments?.getParcelable("user")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout và setup binding
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false)

        // Xử lý khi nhấn nút "Go Home"
        binding.goHomeBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainUserActivity::class.java)
            // Truyền `UserModel` qua Intent
            user?.let {
                intent.putExtra("user", it)
            }
            // Truyền thêm flag thông báo đơn hàng đã được đặt thành công
            intent.putExtra("showOrderPlacedNotification", true)
            startActivity(intent) // Chuyển về MainUserActivity
            dismiss() // Đóng BottomSheet
        }

        return binding.root
    }

    companion object {
        /**
         * Phương thức factory để tạo instance mới của `CongratsBottomSheet`
         * @param user: UserModel cần truyền vào BottomSheet
         * @return Instance của `CongratsBottomSheet`
         */
        fun newInstance(user: UserModel): CongratsBottomSheet {
            return CongratsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable("user", user)
                }
            }
        }
    }
}
