package com.example.grocery.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.Address
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_update_address.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

lateinit var address: Address

class UpdateAddressActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_address)
        sessionManager = SessionManager(this)
        address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address

        init()
    }

    private fun setupToolbar() {
        toolbar.title = "Edit My Address"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                dialogLeave()
            }
        }
        return true
    }

    private fun dialogLeave() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Discard")
        builder.setMessage("Do you want to leave without changing information?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                Toast.makeText(applicationContext, "Discarded", Toast.LENGTH_SHORT).show()
                finish()
            }

        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })

        builder.create().show()
    }


    private fun init() {
        setupToolbar()

        text_view_editaddress_delete.setOnClickListener {
            dialogueDeleteAddress()
        }

        button_editaddress_save.setOnClickListener {
            updateData()

        }
    }

    private fun updateData() {

        var houseNumber = edit_text_editaddress_houseNo.text.toString()
        var street = edit_text_editaddress_street_name.text.toString()
        var city = edit_text_editaddress_city.text.toString()
        var pincode = edit_text_editaddress_pincode.text.toString()
        var type = edit_text_editaddress_type.text.toString()

        if (houseNumber == "" || street == "" || city == "" || pincode.toString() == "" || type == "") {
            dialogueUpdateAddress()
        } else {

            address.houseNo = houseNumber
            address.streetName = street
            address.city = city
            address.pincode = pincode.toInt()
            address.type = type

            var gson = Gson()
            var updateAddress = gson.toJson(address)

            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.PUT,
                Endpoint.updateAddress(address._id),
                JSONObject(updateAddress),
                Response.Listener {
                    Toast.makeText(applicationContext, "Update Successful", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "Update Unsuccessful",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("abc", String(it.networkResponse.data))
                }

            )
            requestQueue.add(request)
        }
    }

    private fun dialogueUpdateAddress() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Update")
        builder.setMessage("You must fill all the blanks.")
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })
        builder.create().show()
    }

    private fun deleteData() {
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.DELETE,
            Endpoint.updateAddress(address._id),
            Response.Listener {
                Toast.makeText(applicationContext, "Deletion Successful", Toast.LENGTH_SHORT)
                    .show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    applicationContext,
                    "deletion unsuccessful",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("abc", String(it.networkResponse.data))
            }
        )
        requestQueue.add(request)
    }

    private fun dialogueDeleteAddress() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this address?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                deleteData()
                finish()
            }
        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })
        builder.create().show()
    }
}



