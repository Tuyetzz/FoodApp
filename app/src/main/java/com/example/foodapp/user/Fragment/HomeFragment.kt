package com.example.foodapp.user.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.user.adapter.MenuAdapter
import com.example.foodapp.user.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup view all menu button
        binding.viewAllMenu.setOnClickListener {
            showBottomSheetDialog()
        }

        // Retrieve and display popular menu items
        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = MenuBottomSheetFragment()
        bottomSheetDialog.show(childFragmentManager, "MenuBottomSheet")
    }

    private fun retrieveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear() // Clear the list to avoid duplicates
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("HomeFragment", "Data retrieved successfully.")
                displayRandomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "Error retrieving data: ${error.message}", error.toException())
                Toast.makeText(requireContext(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayRandomPopularItems() {
        val shuffledItems = menuItems.shuffled()
        val numItemsToShow = 6
        val popularItems = shuffledItems.take(numItemsToShow)

        setPopularItemsAdapter(popularItems)
    }

    private fun setPopularItemsAdapter(items: List<MenuItem>) {
//        val adapter = MenuAdapter(items, requireContext())
//        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.PopularRecyclerView.adapter = adapter
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
