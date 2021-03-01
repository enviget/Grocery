package com.example.grocery.apps

class Endpoint {

    companion object {

        const val URL_CATEGORY = "category"
        const val URL_SUBCATEGORY = "subcategory/"
        const val URL_PRODUCT = "products/sub/"
        const val URL_REGISTER = "auth/register"
        const val URL_LOGIN = "auth/login"
        const val URL_ADDRESS = "address"
        const val URL_ORDER = "orders"
        const val URL_USER = "users/"


        fun getCategory(): String {
            var category = Config.BASE_URL + URL_CATEGORY
            return category
        }

        fun getSubcategory(catId: Int): String {
            var subcategory = "${Config.BASE_URL + URL_SUBCATEGORY + catId}"
            return subcategory
        }

        fun getProduct(subId: Int): String {
            var product = "${Config.BASE_URL + URL_PRODUCT + subId}"
            return product
        }

        fun getRegister(): String{
            return "${Config.BASE_URL + URL_REGISTER}"
        }

        fun getLogin(): String {
            return "${Config.BASE_URL + URL_LOGIN}"
        }

        fun getAddress(): String{
            return "${Config.BASE_URL + URL_ADDRESS}"
        }

        fun postOrder(): String{
            return "${Config.BASE_URL + URL_ORDER}"
        }

        fun getOrder(userId : String?): String{
            return "${Config.BASE_URL + URL_ORDER}/$userId"
        }

        fun updateUser(userId: String): String {
            return "${Config.BASE_URL + URL_USER+userId}"
        }

        fun updateAddress(userId : String) : String{
            return "${Config.BASE_URL + URL_ADDRESS}/$userId"
        }

    }}
