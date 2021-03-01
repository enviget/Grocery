package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.UserResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var sessionManager = SessionManager(this)
        if (sessionManager.login())
        { startActivity(Intent(this,MainActivity::class.java))
        finish()}

        init()
    }

    private fun init() {

        text_view_login_noacc.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        var username = edit_text_login_email.text.toString()
        var password = edit_text_login_password.text.toString()

        var parameter = JSONObject()
        parameter.put("username", username)
        parameter.put("password", password)


        button_login_login.setOnClickListener {
            var username = edit_text_login_email.text.toString()
            var password = edit_text_login_password.text.toString()

            var parameter = JSONObject()
            parameter.put("email", username)
            parameter.put("password", password)

            var requestQueue = Volley.newRequestQueue(this)
            var request = JsonObjectRequest(
                Request.Method.POST,
                Endpoint.getLogin(),
                parameter,
                Response.Listener {
                    var gson = Gson()
                    var userResponse:UserResponse= gson.fromJson(it.toString(), UserResponse::class.java)
                    Log.d("abc", it.toString())

                    var sessionManager = SessionManager(this)
                    sessionManager.saveData(userResponse.user)
                    sessionManager.editor.putString(sessionManager.KEY_PASSWORD,parameter.getString("password")).commit()

                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(applicationContext, "Login", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            )
            requestQueue.add(request)
        }

       text_view_login_skip.setOnClickListener {
           startActivity(Intent(this,MainActivity::class.java))
       }
    }
}