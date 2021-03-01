package com.example.grocery.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.AdapterOrderHistory
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.DBHelper
import com.example.grocery.helpers.SessionManager

import com.example.grocery.models.OrderHistory
import com.example.grocery.models.Product

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderHistoryActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var dbHelper: DBHelper
    var orderList: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)
        init()
    }

    private fun init() {
        getData()
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbar.title = "Order History"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun getData(){
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getOrder(sessionManager.getUser(sessionManager.KEY_ID)),
            Response.Listener {
                var gson = Gson()
                var orderResponse = gson.fromJson(it, OrderHistory::class.java)
                var adapterOrderHistory = AdapterOrderHistory(this)

                for (i in orderResponse.data){
                    orderList.addAll(i.products)
                }
                adapterOrderHistory.setData(orderList)
                recycler_view_order_history.adapter = adapterOrderHistory
                recycler_view_order_history.layoutManager = LinearLayoutManager(this)

                progressBar_order_history.visibility = View.GONE
            },
            Response.ErrorListener {
                Log.d("abc", String(it.networkResponse.data))
            }
        )
        requestQueue.add(request)
    }



}

