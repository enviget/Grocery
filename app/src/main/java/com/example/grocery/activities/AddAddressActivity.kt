package com.example.grocery.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
    }

    private fun init() {
        getData()
        setupToolbar()
    }

    private fun setupToolbar(){
        toolbar.title = "New Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    fun getData(){
        button_addaddress_save.setOnClickListener {
            var streetName = edit_text_addaddress_street_name.text.toString()
            var city = edit_text_addaddress_city.text.toString()
            var houseNo = edit_text_addaddress_houseNo.text.toString()
            var pincode = edit_text_addaddress_pincode.text.toString().toInt()
            var type = edit_text_addaddress_type.text.toString()

            var sessionManager = SessionManager(this)
            var userId = sessionManager.getUser(sessionManager.KEY_ID)

            var parameter = JSONObject()
            parameter.put("streetName", streetName)
            parameter.put("city", city)
            parameter.put("houseNo", houseNo)
            parameter.put("pincode", pincode)
            parameter.put("type", type)
            parameter.put("userId", userId)

            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoint.getAddress(),
                parameter,
                Response.Listener {

                    Toast.makeText(applicationContext, "Added Address", Toast.LENGTH_SHORT).show()
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Add Address Failed", Toast.LENGTH_SHORT).show()
                }
            )
            requestQueue.add(request)
        }
    }
}