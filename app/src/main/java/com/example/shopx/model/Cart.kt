package com.example.shopx.model



// cart response
data class CartResponse(
    val orderId: String,
    val customerId: String,
    val status: String?,
    val address: String?,
    val isInCart: Boolean,
    val note: String?,
    val tel: String?,
    val canceledBy: String?,
    val createdAt: String,
    val deliveredAt: String?,
    val items: List<CartItem>
)
//cart item
data class CartItem(
    val itemId: String,
    val productId: String?,
    val vendorId: String?,
    val size: String,
    val orderId: String? = null,
    val productName: String,
    val quantity: Int,
    val price: String,
    val imageUrl : String,
    val isCancel : Boolean? = false,
    val isActive : Boolean? = false,
    val status: String,
    val createdAt: String,
)

// data class CartItem(
//     val itemId: String,
//     val productId: String?,
//     val vendorId: String?,
//     val size: String,
//     val orderId: String? = null,
//     val productName: String,
//     val quantity: Int,
//     val price: String,
//     val imageUrl : String,
//     val isCancel : Boolean? = false,
//     val isActive : Boolean? = false,
//     val status: String,
//     val createdAt: String,
// )

// cart item details
data class CartItemWithDetails(
    val cartItem: CartItem,
    val product: Product?,
    var quantity: Int = 1 // Default quantity is set to 1
)
