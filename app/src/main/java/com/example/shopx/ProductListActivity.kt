//package com.example.shopx
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.squareup.moshi.JsonClass
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.Types
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@JsonClass(generateAdapter = true)
//data class Product(
//    val id: Int,
//    val name: String,
//    val price: Double,
//    val imageUrl: String
//)
//
//class ProductListActivity : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: ProductAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_main)  // Ensure this matches your XML filename
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recyclerViewProducts)
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
//        adapter = ProductAdapter(emptyList())
//        recyclerView.adapter = adapter
//
//        // Fetch products
//        fetchProducts()
//    }
//
//    private fun fetchProducts() {
//        val client = OkHttpClient()
//
//        val request = Request.Builder()
//            .url("http://[2402:d000:a500:f62:11f7:6b65:8ec4:58a8]/api/v1/Products")
//            .build()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response: Response = client.newCall(request).execute()
//                handleResponse(response)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("ProductList", "Exception: ${e.message}")
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@ProductListActivity, "Failed to fetch products: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private suspend fun handleResponse(response: Response) {
//        withContext(Dispatchers.Main) {
//            val responseBody = response.body?.string()
//            if (response.isSuccessful && responseBody != null) {
//                Log.d("ProductList", "Successful response: $responseBody")
//                val products = parseProducts(responseBody)
//                adapter.updateProducts(products)
//            } else {
//                Log.e("ProductList", "Error response: $responseBody")
//                Log.e("ProductList", "Response code: ${response.code}")
//                Log.e("ProductList", "Response message: ${response.message}")
//                Toast.makeText(this@ProductListActivity, "Failed to fetch products: ${response.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun parseProducts(json: String): List<Product> {
//        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//        val listType = Types.newParameterizedType(List::class.java, Product::class.java)
//        val adapter = moshi.adapter<List<Product>>(listType)
//        return adapter.fromJson(json) ?: emptyList()
//    }
//}
//
//class ProductAdapter(private var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
//
//    class ProductViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
//        val imageView: android.widget.ImageView = view.findViewById(R.id.imageViewProduct)
//        val nameTextView: android.widget.TextView = view.findViewById(R.id.textViewProductName)
//        val priceTextView: android.widget.TextView = view.findViewById(R.id.textViewProductPrice)
//    }
//
//
//
//
//    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ProductViewHolder {
//        val view = android.view.LayoutInflater.from(parent.context)
//            .inflate(R.layout.product_item, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = products[position]
//        holder.nameTextView.text = product.name
//        holder.priceTextView.text = "%.2f".format(product.price)
//
//        // Use a library like Glide or Picasso to load the image
//        // For example, with Glide:
//        // Glide.with(holder.imageView.context).load(product.imageUrl).into(holder.imageView)
//    }
//
//    override fun getItemCount() = products.size
//
//    fun updateProducts(newProducts: List<Product>) {
//        products = newProducts
//        notifyDataSetChanged()
//    }
//
//    fun submitList(it: List<Product>) {
//
//    }
//}
//
