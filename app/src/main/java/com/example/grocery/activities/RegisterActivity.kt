package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        button_register_register.setOnClickListener {
            var name = edit_text_register_name.text.toString()
            var email = edit_text_register_email.text.toString()
            var password = edit_text_register_password.text.toString()
            var mobile = edit_text_register_mobile.text.toString()

            var parameters = JSONObject()
            parameters.put("firstName", name)
            parameters.put("email", email)
            parameters.put("password", password)
            parameters.put("mobile", mobile)

            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoint.getRegister(),
                parameters,
                Response.Listener {
                    var intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        applicationContext,
                        "Successfully Registered",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "fail",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            )
            requestQueue.add(request)
        }

    text_view_register_account.setOnClickListener {
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}}