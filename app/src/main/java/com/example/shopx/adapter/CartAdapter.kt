package com.example.shopx.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopx.R
import com.example.shopx.model.CartItemWithDetails
import com.example.shopx.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(private val apiService: ApiService) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartItemWithDetails> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateCartItems(newCartItems: List<CartItemWithDetails>) {
        this.cartItems = newCartItems
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCartProduct: ImageView = itemView.findViewById(R.id.ivCartProduct)
        private val tvCartProductName: TextView = itemView.findViewById(R.id.tvCartProductName)
        private val tvCartProductPrice: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        private val tvCartItemCount: TextView = itemView.findViewById(R.id.tvCartItemCount)
        private val tvCartProductSize: TextView = itemView.findViewById(R.id.tvCartProductSize)

        fun bind(cartItemWithDetails: CartItemWithDetails) {
            val cartItem = cartItemWithDetails.cartItem
            val productId = cartItem.productId // Use productId to fetch product details

            if (productId != null) {
                fetchProductDetails(productId)
            } else {
                // Handle product not available case
                tvCartProductName.text = "Product Unavailable"
                tvCartProductPrice.text = "N/A"
                ivCartProduct.setImageResource(R.drawable.shoe1) // Corrected typo
                tvCartItemCount.text = "1" // Or however you want to manage count
                tvCartProductSize.text = cartItem.size
            }

            tvCartItemCount.text = "1" // Quantity can be updated based on your data
            tvCartProductSize.text = cartItem.size
        }

        private fun fetchProductDetails(productId: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Fetch product details using the API
                    val productDetails = apiService.getProductById(productId)

                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        // Update the UI with the fetched product details
                        tvCartProductName.text = productDetails.name
                        tvCartProductPrice.text = "$${productDetails.price}"
                        Glide.with(itemView.context)
                            .load(productDetails.imageUrl)
                            .placeholder(R.drawable.shoe1)
                            .into(ivCartProduct)

                        Log.i("String" , "Cart itemefsdf $productDetails")
                    }
                } catch (e: Exception) {
                    Log.e("CartAdapter", "Error fetching product details", e)
                    // Handle the error case
                    withContext(Dispatchers.Main) {
                        tvCartProductName.text = "Error loading product"
                        tvCartProductPrice.text = "N/A"
                        ivCartProduct.setImageResource(R.drawable.shoe1)
                    }
                }
            }
        }
    }
}

