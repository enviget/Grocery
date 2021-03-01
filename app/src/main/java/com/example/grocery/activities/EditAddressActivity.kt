package com.example.grocery.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.AdapterEditAddress
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.Address
import com.example.grocery.models.AddressResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_address.*
import kotlinx.android.synthetic.main.app_bar.*

class EditAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)

        init()
    }

    private fun init() {

        getData()
        setupToolbar()
        image_button_edit_address_add.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

    }

    private fun setupToolbar(){
        toolbar.title = "My Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    private fun getData(){
        var sessionManager = SessionManager(this)
        var userId = sessionManager.getUser(sessionManager.KEY_ID)
        var mAddress = ArrayList<Address>()

        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            "${Endpoint.getAddress()}/$userId",
            Response.Listener {
                var gson = Gson()
                var addressResponse = gson.fromJson(it, AddressResponse::class.java)
                mAddress.addAll(addressResponse.data)
                var adapterEditAddress = AdapterEditAddress(this)
                adapterEditAddress.setData(mAddress)
                recycler_view_edit_address.adapter = adapterEditAddress
                recycler_view_edit_address.layoutManager = LinearLayoutManager(this)
                progressBar_edit_address.visibility= View.GONE
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "Load Address Unsuccessful", Toast.LENGTH_SHORT).show()

            }
        )
        requestQueue.add(request)
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

}