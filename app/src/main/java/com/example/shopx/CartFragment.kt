package com.example.shopx


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.adapter.CartAdapter
import com.example.shopx.model.CartItemWithDetails
import com.example.shopx.model.CartResponse
import com.example.shopx.model.OrderDetails
import com.example.shopx.service.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var tvLastSubTotalItems: TextView
    private lateinit var tvLastSubTotalPrice: TextView
    private lateinit var tvLastTotalPrice: TextView
    private lateinit var btnCartCheckout: Button
    private var cartItemsWithDetails: List<CartItemWithDetails> = emptyList()
    private var currentOrderId: String? = null // Variable to hold the current order ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        sessionManager = SessionManager(requireContext())
        val customerId = sessionManager.getUser()?.id ?: run {
            Log.e("CartFragment", "User data not found in session")
            Toast.makeText(context, "User session error. Please log in again.", Toast.LENGTH_LONG).show()
            return view
        }

        cartRecyclerView = view.findViewById(R.id.rvCartItems)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)

        // Pass the onItemDeleted lambda here
        cartAdapter = CartAdapter(RetrofitInstance.api, customerId, ::onQuantityChanged) { deletedItem ->
            onItemDeleted(deletedItem)
        }

        cartRecyclerView.adapter = cartAdapter

        tvLastSubTotalItems = view.findViewById(R.id.tvLastSubTotalItems)
        tvLastSubTotalPrice = view.findViewById(R.id.tvLastSubTotalprice)
        tvLastTotalPrice = view.findViewById(R.id.tvLastTotalPrice)
        btnCartCheckout = view.findViewById(R.id.btnCartCheckout)

        fetchCartItems(customerId)

        btnCartCheckout.setOnClickListener {
            // Open dialog to get address and phone number
            showCheckoutDialog(currentOrderId)
        }

        return view
    }

    private fun fetchCartItems(customerId: String) {
        RetrofitInstance.api.getCartItems(customerId).enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    val cartResponse = response.body()
                    if (cartResponse != null) {
                        currentOrderId = cartResponse.orderId // Store the order ID from the response
                        fetchProductDetails(cartResponse)
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch cart items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProductDetails(cartResponse: CartResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                cartItemsWithDetails = cartResponse.items.map { cartItem ->
                    val product = cartItem.productId?.let { productId ->
                        try {
                            RetrofitInstance.api.getProductById(productId)
                        } catch (e: Exception) {
                            Log.e("CartFragment", "Error fetching product details: ${e.message}")
                            null
                        }
                    }
                    CartItemWithDetails(cartItem, product)
                }

                withContext(Dispatchers.Main) {
                    cartAdapter.updateCartItems(cartItemsWithDetails)
                    updateTotalPrices()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error fetching product details: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTotalPrices() {
        val totalItems = cartItemsWithDetails.sumOf { it.cartItem.quantity }
        val subTotal = cartItemsWithDetails.sumOf { (it.product?.price ?: 0.0) * it.cartItem.quantity }

        tvLastSubTotalItems.text = "SubTotal Items($totalItems)"
        tvLastSubTotalPrice.text = "$${String.format("%.2f", subTotal)}"
        tvLastTotalPrice.text = "$${String.format("%.2f", subTotal)}" // Assuming no additional fees
    }

    private fun onQuantityChanged(updatedItem: CartItemWithDetails) {
        cartItemsWithDetails = cartItemsWithDetails.map {
            if (it.cartItem.itemId == updatedItem.cartItem.itemId) updatedItem else it
        }
        cartAdapter.updateCartItems(cartItemsWithDetails)
        updateTotalPrices()
    }

    private fun onItemDeleted(deletedItem: CartItemWithDetails) {
        // Check if order ID is available
        currentOrderId?.let { orderId ->
            // Call the deletion API here
            RetrofitInstance.api.deleteCartItem(orderId, deletedItem.cartItem.itemId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Item deleted successfully
                        cartItemsWithDetails = cartItemsWithDetails.filter { it.cartItem.itemId != deletedItem.cartItem.itemId }
                        cartAdapter.updateCartItems(cartItemsWithDetails)
                        updateTotalPrices()
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: Toast.makeText(context, "Order ID not available", Toast.LENGTH_SHORT).show()
    }

    private fun showCheckoutDialog(orderId: String?) {
        if (orderId == null) {
            Toast.makeText(context, "Order ID is not available.", Toast.LENGTH_SHORT).show()
            return
        }
        // Create and show a dialog to collect address and phone number
        val dialog = CheckoutDialog(requireContext()) { address, phone ->
            // Create an OrderRequest object with the address and phone
            val orderRequest = OrderDetails(address,phone)
            // Call the API to place the order
            placeOrder(orderId, orderRequest)
        }
        dialog.show()
    }
    private fun placeOrder(orderId: String, orderRequest: OrderDetails) {
        RetrofitInstance.api.placeOrder(orderId, orderRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


