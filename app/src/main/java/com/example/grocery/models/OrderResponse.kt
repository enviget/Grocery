package com.example.grocery.models
data class OrderHistory(
    val count: Int,
    val data: ArrayList<OrderResponse>,
    val error: Boolean
)

data class OrderResponse(

    var orderSummary: OrderSummary,
    var products: ArrayList<Product>,
    var shippingAddress: ShippingAddress,
    var user: User,
    var userId: String,

    var __v: Int? = null,
    var _id: String?= null,
    var date: String? = null,
    var orderStatus: String? = null
)

data class OrderSummary(
    var _id: String? = null,
    var deliveryCharges: Int? = null,
    var discount: Int? = null,
    var orderAmount: Int? = null,
    var ourPrice: Int? = null,
    var totalAmount: Int? = null
)

data class ShippingAddress(
    var _id: String? = null,
    var city: String? = null,
    var houseNo: String? = null,
    var pincode: Int? = null,
    var streetName: String? = null,
    var type: String? = null
)
