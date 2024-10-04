package com.example.shopx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.adapter.ProductAdapter
import com.example.shopx.model.Product
import com.example.shopx.service.RetrofitInstance
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var recyclerViewResults: RecyclerView

    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf<Product>()         // Original product list
    private val filteredProducts = mutableListOf<Product>()  // Filtered list for display

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        editTextSearch = view.findViewById(R.id.editTextSearch)
        buttonSearch = view.findViewById(R.id.buttonSearch)
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults)

        // Set up RecyclerView
        productAdapter = ProductAdapter(filteredProducts) { product ->
            // Handle product click
        }
        recyclerViewResults.layoutManager = GridLayoutManager(context, 2)
        recyclerViewResults.adapter = productAdapter

        // Fetch products from the API
        fetchProducts()

        // Set up button click listener for search functionality
        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString().trim()
            performSearch(query)
        }
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                // Fetch products from the API
                val fetchedProducts = RetrofitInstance.api.getProducts()
                products.clear()
                products.addAll(fetchedProducts)

                // Initially display all products before filtering
                filteredProducts.clear()
                filteredProducts.addAll(products)
                productAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching products: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun performSearch(query: String) {
        // Clear filtered list and apply the search query
        filteredProducts.clear()

        if (query.isNotEmpty()) {
            filteredProducts.addAll(products.filter { product ->
                product.name?.contains(query, ignoreCase = true) == true
            })
        } else {
            // If query is empty, show all products
            filteredProducts.addAll(products)
        }

        productAdapter.notifyDataSetChanged()

        // Show or hide RecyclerView based on search results
        if (filteredProducts.isNotEmpty()) {
            recyclerViewResults.visibility = View.VISIBLE
        } else {
            recyclerViewResults.visibility = View.GONE
            Toast.makeText(context, "No products found", Toast.LENGTH_SHORT).show()
        }
    }
}
