package com.example.shopx.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopx.R
import com.example.shopx.model.CartItemWithDetails
import com.example.shopx.model.OrderRequest
import com.example.shopx.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(
    private val apiService: ApiService,
    private val customerId: String,
    private val onQuantityChanged: (CartItemWithDetails) -> Unit,
    private val onItemDeleted: (CartItemWithDetails) -> Unit  // Callback for item deletion
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

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
        private val btnCartItemAdd: Button = itemView.findViewById(R.id.btnCartItemAdd)
        private val btnCartItemMinus: Button = itemView.findViewById(R.id.btnCartItemMinus)
        private val btnClose: AppCompatImageButton = itemView.findViewById(R.id.btnClose)  // Delete button

        fun bind(cartItemWithDetails: CartItemWithDetails) {
            val cartItem = cartItemWithDetails.cartItem
            val product = cartItemWithDetails.product

            tvCartProductName.text = product?.name ?: "Product Unavailable"
            tvCartProductPrice.text = "$${product?.price ?: "N/A"}"
            tvCartItemCount.text = cartItem.quantity.toString()
            tvCartProductSize.text = cartItem.size

            Glide.with(itemView.context)
                .load(product?.imageUrl)
                .placeholder(R.drawable.shoe1)
                .into(ivCartProduct)

            btnCartItemAdd.setOnClickListener {
                updateQuantity(cartItemWithDetails, cartItem.quantity + 1)
            }

            btnCartItemMinus.setOnClickListener {
                if (cartItem.quantity > 1) {
                    updateQuantity(cartItemWithDetails, cartItem.quantity - 1)
                }
            }

            btnClose.setOnClickListener {
                deleteCartItem(cartItemWithDetails)
            }
        }

        private fun updateQuantity(cartItemWithDetails: CartItemWithDetails, newQuantity: Int) {
            val cartItem = cartItemWithDetails.cartItem
            val product = cartItemWithDetails.product ?: return

            val orderRequest = OrderRequest(
                customerId = customerId,
                vendorId = product.vendorId,
                productId = product.productId,
                productName = product.name,
                quantity = newQuantity,
                price = product.price.toString(),
                imageUrl = product.imageUrl,
                size = cartItem.size
            )

            apiService.postOrder(orderRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val updatedCartItem = cartItemWithDetails.copy(
                            cartItem = cartItem.copy(quantity = newQuantity)
                        )
                        onQuantityChanged(updatedCartItem)
                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle failure
                }
            })
        }

        private fun deleteCartItem(cartItemWithDetails: CartItemWithDetails) {
            val cartItem = cartItemWithDetails.cartItem
            val product = cartItemWithDetails.cartItem

            // Assuming cartItem has orderId and itemId to delete
            val orderId = cartItem.orderId ?: return
            val itemId = cartItem.itemId
            Log.i("String" , "Order id awa $orderId")
            Log.i("String" , "Item id awa $itemId")
            apiService.deleteCartItem(orderId, itemId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        onItemDeleted(cartItemWithDetails)  // Callback to handle deletion in the activity
                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }
}
