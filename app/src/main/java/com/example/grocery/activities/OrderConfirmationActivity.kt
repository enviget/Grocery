package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.grocery.R
import com.example.grocery.helpers.DBHelper
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.Address
import kotlinx.android.synthetic.main.activity_order_confirmation.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        init()
    }

    private fun init() {

        var dbhelper = DBHelper(this)
        dbhelper.clearTable()
        getData()
        setupToolbar()

        // button redirects user to Main page
        button_receipt_back_to_main.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun getData() {
        // retrieving user address and information
        var address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address

        var sessionManager = SessionManager(this)
        var userName = sessionManager.getUser(sessionManager.KEY_NAME)
        var email = sessionManager.getUser(sessionManager.KEY_EMAIL)
        var mobile = sessionManager.getUser(sessionManager.KEY_MOBILE)


        // User Information
        text_view_receipt_user_name.text = userName
        text_view_receipt_user_mobile.text = mobile
        text_view_receipt_user_email.text = email

        // User Address
        text_view_receipt_street_name.text = address.streetName
        text_view_receipt_city.text = address.city
        text_view_receipt_houseNo.text = address.houseNo
        text_view_receipt_pincode.text = address.pincode.toString()
        text_view_receipt_type.text = address.type
    }



    // set up Toolbar
    private fun setupToolbar() {
        toolbar.title = "Order Confirmation"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //back button redirects user to Main page
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
        return true
    }

}