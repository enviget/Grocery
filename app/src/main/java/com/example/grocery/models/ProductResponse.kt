package com.example.grocery.models

import java.io.Serializable

data class ProductResponse(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
)

data class Product(
    var __v: Int? = null,
    var _id: String? = null,
    var catId: Int? = null,
    var created: String? = null,
    var description: String? = null,
    var image: String? = null,
    var mrp: Double? = null,
    var position: Int? = null,
    var price: Double? = null,
    var productName: String? = null,
    var quantity: Int = 0,
    var status: Boolean? = null,
    var subId: Int? = null,
    var unit: String? = null
):Serializable{

    companion object{
        const val KEY_PRODUCT = "product"
    }
}