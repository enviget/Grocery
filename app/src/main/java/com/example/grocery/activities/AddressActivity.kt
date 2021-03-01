package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.AdapterAddress
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.Address
import com.example.grocery.models.AddressResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.app_bar.*

class AddressActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        init()
    }

    private fun init() {

        getData()
        setupToolbar()

        button_edit_address.setOnClickListener {
            startActivity(Intent(this, EditAddressActivity::class.java))
        }
    }

    private fun setupToolbar(){
        toolbar.title = "Shipping Address"
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
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_homepage -> startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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
            var adapterAddress = AdapterAddress(this)
            adapterAddress.setData(mAddress)
            recycler_view_address.adapter = adapterAddress
            recycler_view_address.layoutManager = LinearLayoutManager(this)
            progressBar_address.visibility = View.GONE
        },
        Response.ErrorListener {
            Toast.makeText(applicationContext, "Get Address failed", Toast.LENGTH_SHORT).show()
        }
    )
    requestQueue.add(request)
}
    override fun onResume() {
        getData()
        super.onResume()

    }

}