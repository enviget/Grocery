package com.example.grocery.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject

class MyAccountActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var edit: ArrayList<String>
    lateinit var KEY: String
    lateinit var newInfo: String
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        edit = ArrayList()
        edit.clear()
        KEY = ""
        newInfo = ""
        sessionManager = SessionManager(this)
        init()
    }

    private fun init() {

        text_view_myaccount_mobile.text = sessionManager.getUser(sessionManager.KEY_MOBILE)
        text_view_myaccount_name.text = sessionManager.getUser(sessionManager.KEY_NAME)
        text_view_myaccount_password.text = sessionManager.getUser(sessionManager.KEY_PASSWORD)
        text_view_myaccount_email.text = sessionManager.getUser(sessionManager.KEY_EMAIL)


        onClickbuttons()
        setupToolbar()

    }

    private fun setupToolbar(){
        toolbar.title = "My Account"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
        android.R.id.home -> {
            if (button_myaccount_update.visibility == View.VISIBLE)
                dialogAlert()
            else
                finish()
        }}
    return true}

    private fun onClickbuttons() {
        button_myaccount_edit_name.setOnClickListener(this)
        button_myaccount_edit_email.setOnClickListener(this)
        button_myaccount_edit_password.setOnClickListener(this)
        button_myaccount_edit_mobile.setOnClickListener(this)
        button_myaccount_update.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            button_myaccount_edit_name -> {
                button_myaccount_edit_name.visibility = View.GONE
                button_myaccount_edit_email.visibility = View.GONE
                button_myaccount_edit_password.visibility = View.GONE
                button_myaccount_edit_mobile.visibility = View.GONE

                text_view_myaccount_name.visibility = View.GONE
                button_myaccount_update.visibility = View.VISIBLE

                KEY = "firstName"

            }
            button_myaccount_edit_email -> {
                button_myaccount_edit_name.visibility = View.GONE
                button_myaccount_edit_email.visibility = View.GONE
                button_myaccount_edit_password.visibility = View.GONE
                button_myaccount_edit_mobile.visibility = View.GONE

                text_view_myaccount_email.visibility = View.GONE
                button_myaccount_update.visibility = View.VISIBLE

                KEY = "email"
            }
            button_myaccount_edit_password -> {
                button_myaccount_edit_name.visibility = View.GONE
                button_myaccount_edit_email.visibility = View.GONE
                button_myaccount_edit_password.visibility = View.GONE
                button_myaccount_edit_mobile.visibility = View.GONE

                text_view_myaccount_password.visibility = View.GONE
                button_myaccount_update.visibility = View.VISIBLE

                KEY = "password"
            }
            button_myaccount_edit_mobile -> {
                button_myaccount_edit_name.visibility = View.GONE
                button_myaccount_edit_email.visibility = View.GONE
                button_myaccount_edit_password.visibility = View.GONE
                button_myaccount_edit_mobile.visibility = View.GONE

                text_view_myaccount_mobile.visibility = View.GONE
                button_myaccount_update.visibility = View.VISIBLE

                KEY = "mobile"
            }

            button_myaccount_update -> {

                edit.add(edit_text_myaccount_email.text.toString())
                edit.add(edit_text_myaccount_password.text.toString())
                edit.add(edit_text_myaccount_email.text.toString())
                edit.add(edit_text_myaccount_mobile.text.toString())

                for (i in edit.indices) {
                    if (edit[i] != "") {
                        newInfo = edit[i]
                    }
                }

                if (newInfo == "")
                    dialogAlert()
                else{
                var user = User()
                when (KEY) {
                    "firstName" -> user.firstName = newInfo
                    "mobile" -> user.mobile = newInfo
                    "password" -> user.password = newInfo
                    "email" -> user.email = newInfo
                }
                var gson = Gson()
                var updateUser = gson.toJson(user)

                var requestQueue = Volley.newRequestQueue(this)
                var request = JsonObjectRequest(
                    Request.Method.PUT,
                    Endpoint.updateUser(sessionManager.getUser(sessionManager.KEY_ID).toString()),
                    JSONObject(updateUser),
                    Response.Listener {
                        default()

                        Toast.makeText(applicationContext, "update Successful", Toast.LENGTH_SHORT)
                            .show()

                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            applicationContext,
                            "update unsuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("abc", String(it.networkResponse.data))
                    }

                )
                requestQueue.add(request)
            }
        }
    }}

    private fun dialogAlert() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Discard")
        builder.setMessage("Do you want to leave without changing information?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                default()
            }

        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })

        builder.create().show()
    }

    private fun default(){
        edit_text_myaccount_mobile.text.clear()
        edit_text_myaccount_email.text.clear()
        edit_text_myaccount_name.text.clear()
        edit_text_myaccount_password.text.clear()

        button_myaccount_update.visibility = View.GONE
        button_myaccount_edit_name.visibility = View.VISIBLE
        button_myaccount_edit_email.visibility = View.VISIBLE
        button_myaccount_edit_password.visibility = View.VISIBLE
        button_myaccount_edit_mobile.visibility = View.VISIBLE
        text_view_myaccount_name.visibility = View.VISIBLE
        text_view_myaccount_email.visibility = View.VISIBLE
        text_view_myaccount_mobile.visibility = View.VISIBLE
        text_view_myaccount_password.visibility = View.VISIBLE
    }
}