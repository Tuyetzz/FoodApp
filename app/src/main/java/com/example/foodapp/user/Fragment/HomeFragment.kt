package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.user.adapter.MenuAdapter
import com.example.foodapp.user.model.MenuItem
import com.example.foodapp.user.view.MainUserActivity
import com.example.foodapp.user.viewmodel.SharedViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: FirebaseFirestore

    // SharedViewModel sử dụng activityViewModels để lấy ViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup view all menu button
        binding.viewAllMenu.setOnClickListener {
            showBottomSheetDialog()
        }

        // Lấy và hiển thị danh sách bán chạy nhất
        retrieveTopOrderedItems()

        return binding.root
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = MenuBottomSheetFragment()
        bottomSheetDialog.show(childFragmentManager, "MenuBottomSheet")
    }

    private fun retrieveTopOrderedItems() {
        db = FirebaseFirestore.getInstance()

        // Truy vấn bảng orderedItems để lấy số lượng bán của từng item
        db.collection("orderedItems")
            .get()
            .addOnSuccessListener { snapshot ->
                val itemCounts = mutableMapOf<String, Int>()

                // Tổng hợp số lượng bán theo từng itemId
                for (document in snapshot.documents) {
                    val itemId = document.getString("itemId")
                    val quantity = document.getLong("quantity")?.toInt() ?: 0
                    if (itemId != null) {
                        itemCounts[itemId] = itemCounts.getOrDefault(itemId, 0) + quantity
                    }
                }

                // Sắp xếp các món theo số lượng bán giảm dần
                val topItems = itemCounts.entries.sortedByDescending { it.value }.take(6)

                // Lấy thông tin chi tiết từ bảng items
                fetchItemDetails(topItems.map { it.key })
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "Error fetching ordered items: ${e.message}")
                Toast.makeText(requireContext(), "Failed to load popular items.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchItemDetails(topItemIds: List<String>) {
        if (topItemIds.isEmpty()) {
            Log.d("HomeFragment", "No item IDs to fetch.")
            return
        }

        db.collection("items")
            .whereIn("itemId", topItemIds)
            .get()
            .addOnSuccessListener { snapshot ->
                val popularItems = snapshot.toObjects(MenuItem::class.java)

                // Kiểm tra nếu danh sách rỗng
                if (popularItems.isEmpty()) {
                    Log.d("HomeFragment", "No item details found for popular items.")
                    binding.PopularRecyclerView.visibility = View.GONE
                    return@addOnSuccessListener
                }

                // Hiển thị danh sách lên RecyclerView
                setPopularItemsAdapter(popularItems)
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "Error fetching item details: ${e.message}")
                Toast.makeText(requireContext(), "Failed to load popular items.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setPopularItemsAdapter(items: List<MenuItem>) {
        // Giới hạn danh sách tối đa 3 món
        val limitedItems = items.take(3)

        // Khởi tạo adapter với SharedViewModel
        val adapter = MenuAdapter(
            menuItems = limitedItems,
            context = requireContext(),
            sharedViewModel = sharedViewModel // Lấy trực tiếp từ sharedViewModel
        )

        // Thiết lập RecyclerView theo chiều dọc
        binding.PopularRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
            visibility = View.VISIBLE
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupImageSlider()
    }

    private fun setupImageSlider() {
        val imageList = arrayListOf(
            SlideModel(R.drawable.banner1, ScaleTypes.FIT),
            SlideModel(R.drawable.banner2, ScaleTypes.FIT),
            SlideModel(R.drawable.banner3, ScaleTypes.FIT)
        )

        binding.imageSlider.apply {
            setImageList(imageList, ScaleTypes.FIT)
            setItemClickListener(object : ItemClickListener {
                override fun doubleClick(position: Int) {
                    Toast.makeText(requireContext(), "Double clicked on image $position", Toast.LENGTH_SHORT).show()
                }

                override fun onItemSelected(position: Int) {
                    val message = "Selected Image $position"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
