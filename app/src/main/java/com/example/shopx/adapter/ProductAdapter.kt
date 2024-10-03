package com.example.shopx.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopx.databinding.ProductItemBinding
import com.example.shopx.model.Product

class ProductAdapter(private val products: List<Product>, private val onLikeClicked: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, onLikeClicked: (Product) -> Unit) {
            binding.apply {
                textViewProductName.text = product.name
                textViewProductPrice.text = String.format("$%.2f", product.price)
                textViewProductDescription.text = product.description
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
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], onLikeClicked)
    }

    override fun getItemCount() = products.size
}