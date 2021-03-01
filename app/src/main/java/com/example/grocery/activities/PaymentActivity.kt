package com.example.grocery.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.DBHelper
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {

    lateinit var address: Address
    lateinit var sessionManager: SessionManager
    lateinit var dbhelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address
        sessionManager = SessionManager(this)
        dbhelper = DBHelper(this)
        init()

    }

    private fun init() {
        setupToolbar()

        radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{group, checkedId -> val radio:RadioButton = findViewById(checkedId)})
        button_payment_finish.setOnClickListener {

            var id: Int = radio_group.checkedRadioButtonId
            when(id){
                R.id.radio_button_1 -> dialogAlert()
                R.id.button_payment_cash -> postData()
                else ->{
                    dialogAlert()
                }

            }
        }

    }

    private fun dialogAlert() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Payment")
        builder.setMessage("Please choose your payment")
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })
        builder.create().show()
    }


    private fun setupToolbar() {
        toolbar.title = "Payment Method"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var cart = menu.findItem(R.id.menu_cart)
        cart.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_homepage -> startActivity(
                Intent(this, MainActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
        return true
    }

    private fun postData() {

        var orderSummary = getOrderSummary()
        var shippingAddress = getShippingAddress()
        var product = getProduct()
        var user = getUser()
        var userId = sessionManager.getUser(sessionManager.KEY_ID).toString()

        var orderResponse = OrderResponse(
            orderSummary = orderSummary,
            shippingAddress = shippingAddress,
            products = product,
            userId = userId,
            user = user
        )
        var gson = Gson()
        var order = gson.toJson(orderResponse)

        var requestQueue = Volley.newRequestQueue(this)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoint.postOrder(),
            JSONObject(order),
            Response.Listener {

                Toast.makeText(applicationContext, "Order Successful", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, OrderConfirmationActivity::class.java)
                intent.putExtra(Address.KEY_ADDRESS, address)
                startActivity(intent)
                finish()


            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Order unsuccessful", Toast.LENGTH_SHORT).show()
                Log.d("abc", String(it.networkResponse.data))
            }

        )
        requestQueue.add(request)
    }

    private fun getOrderSummary(): OrderSummary {
        var sharedPreferences = getSharedPreferences(sessionManager.FILE_NAME, Context.MODE_PRIVATE)
        var total = sharedPreferences.getInt(sessionManager.KEY_TOTAL, 0)
        var discount = sharedPreferences.getInt(sessionManager.KEY_DISCOUNT, 0)


        var orderSummary = OrderSummary()
        orderSummary._id = sessionManager.getUser(sessionManager.KEY_ID)
        orderSummary.deliveryCharges = 0
        orderSummary.discount = discount
        orderSummary.orderAmount = dbhelper.getTotalQuantity()
        orderSummary.ourPrice = total
        orderSummary.totalAmount = dbhelper.getMRP().toInt()


        return orderSummary
    }

    private fun getShippingAddress(): ShippingAddress {
        var shippingAddress = ShippingAddress()
        shippingAddress._id = address._id
        shippingAddress.city = address.city
        shippingAddress.houseNo = address.houseNo
        shippingAddress.pincode = address.pincode
        shippingAddress.streetName = address.streetName
        shippingAddress.type = address.type

        return shippingAddress
    }

    private fun getProduct(): ArrayList<Product> {
        var orderProduct = dbhelper.readData()
        return orderProduct
    }

    private fun getUser(): User {
        var user = sessionManager.user()
        return user
    }

}