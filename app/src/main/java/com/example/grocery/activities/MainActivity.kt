package com.example.grocery.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.AdapterCategory
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.DBHelper
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.Category
import com.example.grocery.models.CategoryResponse
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.content_main.recycler_view as recycler_view1

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var textViewCartCount: TextView? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    var categoryList: ArrayList<Category> = ArrayList()
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)
        init()
    }

    private fun init() {
        setupToolbar()
        updateCartCount()
        setupDrawer()
        getData()
    }



    private fun setupToolbar() {
        toolbar.title = "Home"
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var menu_view = MenuItemCompat.getActionView(item)
        textViewCartCount = menu_view.text_view_cart_count
        menu_view.setOnClickListener {
            startActivity(Intent(this, CheckOutCartActivity::class.java))
        }

        var home = menu.findItem(R.id.menu_homepage)
        home.isVisible = false

        updateCartCount()
        return true
    }

    private fun updateCartCount() {
        var dbhelper = DBHelper(this)
        var count = dbhelper.getTotalQuantity()
        if (count == 0)
            textViewCartCount?.visibility = View.GONE
        else {
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = count.toString()
        }
    }

    private fun setupDrawer() {
        drawerLayout = drawer_layout
        navView = nav_view

        var headerView = navView.getHeaderView(0)
        sessionManager = SessionManager(this)
        navView.setNavigationItemSelectedListener(this)
        if (sessionManager.login()) {
            headerView.nav_view_email.text = "${sessionManager.getUser(sessionManager.KEY_EMAIL)}"
            headerView.nav_view_name.text = "${sessionManager.getUser(sessionManager.KEY_NAME)}"
        }

        var toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            dialogueClose()

        }
    }
    private fun dialogueClose() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you want to close this application?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                finishAffinity()
            }
        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                dialogue?.dismiss()
            }
        })
        builder.create().show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_account -> startActivity(Intent(this, MyAccountActivity::class.java))

            R.id.nav_setting -> Toast.makeText(applicationContext, "setting", Toast.LENGTH_SHORT)
                .show()
            R.id.nav_orderhisotry -> startActivity(Intent(this, OrderHistoryActivity::class.java))
            R.id.nav_address ->{
                if (sessionManager.login())
                    startActivity(Intent(this, EditAddressActivity::class.java))
                else
                    dialogLogin()
            }
            R.id.nav_get_help -> Toast.makeText(applicationContext, "Get Help", Toast.LENGTH_SHORT)
                .show()
            R.id.nav_logout -> {
                if(sessionManager.login())
                    dialogueLogout()
                else
                    startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun dialogLogin() {
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to login?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }

        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                dialog?.dismiss()
            }
        })
        builder.create().show()
    }

    private fun dialogueLogout() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialogue: DialogInterface?, p1: Int) {
                sessionManager.logout()
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
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

    fun getData() {
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getCategory(),
            Response.Listener {
                var gson = Gson()
                var categoryResponse = gson.fromJson(it, CategoryResponse::class.java)
                categoryList.addAll(categoryResponse.data)
                var adapterCategory = AdapterCategory(this)
                adapterCategory.setData(categoryList)
                recycler_view1.adapter = adapterCategory
                recycler_view1.layoutManager = GridLayoutManager(this,2)

                progressBar_home.visibility = View.GONE
            },
            Response.ErrorListener { }
        )
        requestQueue.add(request)
    }

    override fun onResume() {
        super.onResume()
        updateCartCount()
    }
}