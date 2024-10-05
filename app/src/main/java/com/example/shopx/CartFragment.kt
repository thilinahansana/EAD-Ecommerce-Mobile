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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        sessionManager = SessionManager(requireContext())

        cartRecyclerView = view.findViewById(R.id.rvCartItems)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(apiService = RetrofitInstance.api)
        cartRecyclerView.adapter = cartAdapter

        tvLastSubTotalItems = view.findViewById(R.id.tvLastSubTotalItems)
        tvLastSubTotalPrice = view.findViewById(R.id.tvLastSubTotalprice)
        tvLastTotalPrice = view.findViewById(R.id.tvLastTotalPrice)
        btnCartCheckout = view.findViewById(R.id.btnCartCheckout)

        fetchCartItems()

        btnCartCheckout.setOnClickListener {
            Toast.makeText(context, "Checkout functionality not implemented yet", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchCartItems() {
        val user = sessionManager.getUser()

        if (user == null) {
            Log.e("CartFragment", "User data not found in session")
            Toast.makeText(context, "User session error. Please log in again.", Toast.LENGTH_LONG).show()
            return
        }

        val customerId = user.id

        if (customerId != null) {
            RetrofitInstance.api.getCartItems(customerId).enqueue(object : Callback<CartResponse> {
                override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                    if (response.isSuccessful) {
                        val cartResponse = response.body()
                        if (cartResponse != null) {
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
        } else {
            Toast.makeText(context, "Customer ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchProductDetails(cartResponse: CartResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartItemsWithDetails = cartResponse.items.map { cartItem ->
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
                    updateTotalPrices(cartItemsWithDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error fetching product details: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTotalPrices(cartItemsWithDetails: List<CartItemWithDetails>) {
        val totalItems = cartItemsWithDetails.size
        val subTotal = cartItemsWithDetails.sumOf { it.product?.price ?: 0.0 }

        tvLastSubTotalItems.text = "SubTotal Items($totalItems)"
        tvLastSubTotalPrice.text = "$${String.format("%.2f", subTotal)}"
        tvLastTotalPrice.text = "$${String.format("%.2f", subTotal)}" // Assuming no additional fees
    }
}