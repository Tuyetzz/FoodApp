package com.example.foodapp.user.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityPayOutBinding
import com.example.foodapp.user.Fragment.CongratsBottomSheet
import com.example.foodapp.user.model.Order
import com.example.foodapp.user.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận Order từ Intent
        val order: Order? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("order", Order::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("order")
        }

        if (order != null) {
            Log.d("PayOutActivity", "Order received: $order")
            populateFieldsFromOrder(order)
            setupPaymentSpinner()
            setupCheckDiscountButton(order)
            calculateTotalAmount(order)
            setupPlaceOrderButton(order) // Gắn sự kiện cho nút PlaceMyOrderBtn
            logOrderDetails(order)
        } else {
            Log.e("PayOutActivity", "No Order received!")
            clearFields()
        }

        binding.discountEditText.setText("")
    }

    private fun populateFieldsFromOrder(order: Order) {
        order.client?.let { client ->
            binding.nameEditText.setText(client.fullName ?: "")
            binding.addressEditText.setText(client.address ?: "")
            binding.phoneEditText.setText(client.phone ?: "")
        } ?: run {
            clearFields()
        }
    }

    private fun clearFields() {
        binding.nameEditText.setText("")
        binding.addressEditText.setText("")
        binding.phoneEditText.setText("")
        binding.discountEditText.setText("")
    }

    private fun setupPaymentSpinner() {
        val paymentMethods = listOf("Cash", "Card")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentMethodSpinner.adapter = adapter
    }

    private fun setupCheckDiscountButton(order: Order) {
        binding.checkDiscountButton.setOnClickListener {
            val discountCode = binding.discountEditText.text.toString().trim()
            if (discountCode.isNotEmpty()) {
                checkDiscountCode(discountCode, order)
            } else {
                Toast.makeText(this, "Please enter a discount code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkDiscountCode(code: String, order: Order) {
        db.collection("discounts")
            .whereEqualTo("code", code)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    binding.discountEditText.error = "Invalid discount code"
                    Toast.makeText(this, "Invalid discount code", Toast.LENGTH_SHORT).show()
                } else {
                    val document = querySnapshot.documents.first()
                    val discountPercentage = document.getLong("percentage")?.toInt() ?: 0
                    val expiryDateString = document.getString("expiryDate") ?: "N/A"

                    val expiryDate = parseDateToMillis(expiryDateString)
                    val currentDate = System.currentTimeMillis()

                    if (expiryDate != null && currentDate > expiryDate) {
                        binding.discountEditText.error = "Discount code expired"
                        Toast.makeText(this, "Discount code expired", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    order.discount = order.discount?.apply {
                        discountId = document.id
                        this.code = code
                        this.percentage = discountPercentage.toFloat()
                        this.expiryDate = expiryDateString
                    } ?: com.example.foodapp.user.model.Discount(
                        discountId = document.id,
                        code = code,
                        percentage = discountPercentage.toFloat(),
                        expiryDate = expiryDateString
                    )

                    calculateTotalAmount(order)
                    Toast.makeText(this, "Discount applied: $discountPercentage%", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error checking discount code", Toast.LENGTH_SHORT).show()
            }
    }

    private fun parseDateToMillis(dateString: String): Long? {
        return try {
            val dateFormat = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
            val date = dateFormat.parse(dateString)
            date?.time
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateTotalAmount(order: Order) {
        val totalWithoutDiscount = order.listOrderedItem?.sumOf { orderedItem ->
            val price = orderedItem.item?.itemPrice ?: 0.0
            val quantity = orderedItem.quantity ?: 1
            price * quantity
        } ?: 0.0

        val discountPercentage = order.discount?.percentage?.toDouble() ?: 0.0
        val discountAmount = (totalWithoutDiscount * (discountPercentage / 100.0))
        val totalWithDiscount = (totalWithoutDiscount - discountAmount).coerceAtLeast(0.0)

        if (discountPercentage > 0.0) {
            binding.totalAmountTextView.text = String.format(
                "$%.2f (-%.0f%%)", totalWithDiscount, discountPercentage
            )
        } else {
            binding.totalAmountTextView.text = String.format("$%.2f", totalWithoutDiscount)
        }
    }

    private fun setupPlaceOrderButton(order: Order) {
        binding.PlaceMyOrderBtn.setOnClickListener {
            order.id = UUID.randomUUID().toString() // Tạo ID duy nhất cho đơn hàng
            order.paymentType = binding.paymentMethodSpinner.selectedItem?.toString()
            order.orderStatus = "Pending"

            // Lưu Order trước
            db.collection("orders").document(order.id!!)
                .set(order)
                .addOnSuccessListener {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                    Log.d("PayOutActivity", "Order saved to Firestore: $order")

                    saveOrderedItems(order)
                    showCongratsBottomSheet(order.client)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show()
                    Log.e("PayOutActivity", "Error saving order", exception)
                }
        }
    }
    private fun showCongratsBottomSheet(user: UserModel?) {
        val bottomSheet = CongratsBottomSheet.newInstance(user!!)
        bottomSheet.show(supportFragmentManager, "CongratsBottomSheet")
    }

    private fun saveOrderedItems(order: Order) {
        val orderedItemsCollection = db.collection("orderedItems")
        order.listOrderedItem?.forEach { orderedItem ->
            val orderedItemId = UUID.randomUUID().toString() // Tạo ID duy nhất cho OrderedItem
            val quantity = orderedItem.quantity ?: 1
            val itemPrice = orderedItem.item?.itemPrice ?: 0.0
            val totalPrice = quantity * itemPrice // Tổng tiền = số lượng * giá mỗi item

            val orderedItemData = hashMapOf(
                "id" to orderedItemId,
                "orderId" to order.id, // Liên kết OrderedItem với Order
                "itemId" to orderedItem.item?.itemId,
                "quantity" to quantity,
                "price" to totalPrice, // Lưu tổng tiền cho OrderedItem
                "orderDate" to orderedItem.orderDate
            )

            orderedItemsCollection.document(orderedItemId)
                .set(orderedItemData)
                .addOnSuccessListener {
                    Log.d("PayOutActivity", "OrderedItem saved: $orderedItemData")
                }
                .addOnFailureListener { exception ->
                    Log.e("PayOutActivity", "Error saving OrderedItem: $orderedItemData", exception)
                }
        }
    }


    private fun logOrderDetails(order: Order) {
        Log.d("PayOutActivity", "Order Details:")
        Log.d("PayOutActivity", "Order ID: ${order.id ?: "N/A"}")
        Log.d("PayOutActivity", "Order Status: ${order.orderStatus ?: "N/A"}")
        Log.d("PayOutActivity", "Payment Type: ${order.paymentType ?: "N/A"}")

        order.client?.let { client ->
            Log.d("PayOutActivity", "Client Details:")
            Log.d("PayOutActivity", "  Client ID: ${client.id ?: "N/A"}")
            Log.d("PayOutActivity", "  Client Name: ${client.fullName ?: "N/A"}")
            Log.d("PayOutActivity", "  Client Address: ${client.address ?: "N/A"}")
            Log.d("PayOutActivity", "  Client Username: ${client.username ?: "N/A"}")
            Log.d("PayOutActivity", "  Client Phone: ${client.phone ?: "N/A"}")
            Log.d("PayOutActivity", "  Client Role: ${client.role ?: "N/A"}")
        } ?: Log.d("PayOutActivity", "Client: N/A")

        order.manager?.let { manager ->
            Log.d("PayOutActivity", "Manager Details:")
            Log.d("PayOutActivity", "  ManagerID: ${manager.id ?: "N/A"}")
            Log.d("PayOutActivity", "  ManagerName: ${manager.fullName ?: "N/A"}")
            Log.d("PayOutActivity", "  ManagerAddress: ${manager.address ?: "N/A"}")
            Log.d("PayOutActivity", "  ManagerUsername: ${manager.username ?: "N/A"}")
            Log.d("PayOutActivity", "  ManagerPhone: ${manager.phone ?: "N/A"}")
            Log.d("PayOutActivity", "  ManagerRole: ${manager.role ?: "N/A"}")
        } ?: Log.d("PayOutActivity", "Manager: N/A")

        order.discount?.let { discount ->
            Log.d("PayOutActivity", "Discount Details:")
            Log.d("PayOutActivity", "  Discount ID: ${discount.discountId ?: "N/A"}")
            Log.d("PayOutActivity", "  Code: ${discount.code ?: "N/A"}")
            Log.d("PayOutActivity", "  Percentage: ${discount.percentage}")
            Log.d("PayOutActivity", "  Expiry Date: ${discount.expiryDate ?: "N/A"}")
        } ?: Log.d("PayOutActivity", "Discount: N/A")

        order.listOrderedItem?.forEach { orderedItem ->
            Log.d("PayOutActivity", "Ordered Item Details:")
            Log.d("PayOutActivity", "  Ordered Item ID: ${orderedItem.id ?: "N/A"}")
            Log.d("PayOutActivity", "  Quantity: ${orderedItem.quantity ?: "N/A"}")
            Log.d("PayOutActivity", "  Order Date: ${orderedItem.orderDate ?: "N/A"}")

            orderedItem.item?.let { menuItem ->
                Log.d("PayOutActivity", "  Menu Item Details:")
                Log.d("PayOutActivity", "    Item ID: ${menuItem.itemId}")
                Log.d("PayOutActivity", "    Item Name: ${menuItem.itemName}")
                Log.d("PayOutActivity", "    Item Price: ${menuItem.itemPrice}")
                Log.d("PayOutActivity", "    Short Description: ${menuItem.shortDescription}")
                Log.d("PayOutActivity", "    Item Image URL: ${menuItem.itemImage}")
                Log.d("PayOutActivity", "    Ingredients: ${menuItem.ingredients.joinToString(", ")}")
            } ?: Log.d("PayOutActivity", "  Menu Item: N/A")
        } ?: Log.d("PayOutActivity", "No Ordered Items")

    }
}
