// ProductAdapter.kt

package com.example.shopx.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopx.ProductDetailsActivity
import com.example.shopx.databinding.ProductItemBinding
import com.example.shopx.model.Product

class ProductAdapter(private val products: List<Product>, private val onLikeClicked: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.name
                textViewProductPrice.text = String.format("$%.2f", product.price)
                textViewStockQuantity.text = "In stock: ${product.stockQuantity}"
                btnLike.isChecked = product.isLiked

                // Load image using Glide
                Glide.with(imageViewProduct.context)
                    .load(product.imageUrl)
                    .into(imageViewProduct)

                btnLike.setOnClickListener {
                    product.isLiked = !product.isLiked
                    onLikeClicked(product)
                }

                // Start ProductDetailsActivity on item click
                itemView.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, ProductDetailsActivity::class.java).apply {
                        putExtra("productName", product.name)
                        putExtra("productDescription", product.description)
                        putExtra("productPrice", product.price)
                        putExtra("productImage", product.imageUrl)
                        putExtra("productStockQuantity", product.stockQuantity)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}
