// HomeFragment.kt

package com.example.shopx
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopx.adapter.ProductAdapter
import com.example.shopx.databinding.FragmentMainBinding
import com.example.shopx.model.Product
import com.example.shopx.service.RetrofitInstance
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf<Product>()
    private val filteredProducts = mutableListOf<Product>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupMenu()
        setupRecyclerView()
        setupFilterButton()
        fetchProducts()

        Log.i("String" , "Product $products" )
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbarmenu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_notification -> {
                        val intent = Intent(context, NotificationsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(filteredProducts) { product ->
            // Navigate to ProductDetailsActivity with product data
            val intent = Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra("productId" , product.productId)
                putExtra("vendorId", product.vendorId)
                putExtra("productName", product.name)
                putExtra("productDescription", product.description)
                putExtra("productPrice", product.price)
                putExtra("productImage", product.imageUrl)
                putExtra("productStockQuantity", product.stockQuantity)
            }
            startActivity(intent)
        }
        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            val filterBottomSheet = FilterBottomSheetFragment { type, size, minPrice, maxPrice ->
                applyFilters(type, size, minPrice, maxPrice)
            }
            filterBottomSheet.show(parentFragmentManager, filterBottomSheet.tag)
        }
    }


    private fun applyFilters(type: String, size: String, minPrice: Double?, maxPrice: Double?) {
        filteredProducts.clear()
        filteredProducts.addAll(products.filter { product ->
            val matchesType = type == "All" || product.type.equals(type, ignoreCase = true)
            val matchesSize = size == "All" || product.size.equals(size, ignoreCase = true)
            val matchesMinPrice = minPrice == null || product.price >= minPrice
            val matchesMaxPrice = maxPrice == null || product.price <= maxPrice

            matchesType && matchesSize && matchesMinPrice && matchesMaxPrice
        })
        productAdapter.notifyDataSetChanged()
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            try {
                val fetchedProducts = RetrofitInstance.api.getProducts()
                products.clear()
                products.addAll(fetchedProducts)
                filteredProducts.clear()
                filteredProducts.addAll(products)
                productAdapter.notifyDataSetChanged()

                Log.i("String " , "gelooooo $fetchedProducts")
            } catch (e: Exception) {
                Toast.makeText(context, "Error fetching products: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
