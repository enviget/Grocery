package com.example.grocery.models

import java.io.Serializable
data class Edit(
    val error: Boolean,
    val message: String
)

data class AddressResponse(
    val count: Int,
    val data: ArrayList<Address>,
    val error: Boolean
)

data class Address(
    var __v: Int,
    var _id: String,
    var city: String,
    var houseNo: String,
    var pincode: Int,
    var streetName: String,
    var type: String,
    var userId: String
):
        Serializable{
    companion object{
        const val KEY_ADDRESS = "address"
    }
}