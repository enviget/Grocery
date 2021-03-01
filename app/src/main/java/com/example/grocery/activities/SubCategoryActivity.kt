package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.MyFragmentAdapter
import com.example.grocery.apps.Endpoint
import com.example.grocery.helpers.DBHelper
import com.example.grocery.models.Category
import com.example.grocery.models.SubCategory
import com.example.grocery.models.SubCategoryResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class SubCategoryActivity : AppCompatActivity() {

    var subcategoryList = ArrayList<SubCategory>()
    private var textViewCartCount : TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        init()
    }

    private fun init() {

        generateData()
        updateCartCount()

    }

    private fun setupToolbar(category: Category){
        toolbar.title = category.catName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item = menu?.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view= MenuItemCompat.getActionView(item)
        textViewCartCount = view.text_view_cart_count
        view.setOnClickListener{
            startActivity(Intent(this,CheckOutCartActivity::class.java))
        }
        updateCartCount()
        return true
    }


    private fun updateCartCount(){
        var dbhelper = DBHelper(this)
        var count = dbhelper.getTotalQuantity()
        if(count==0){
            textViewCartCount?.visibility = View.GONE
        }
        else{
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = count.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.menu_homepage -> startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))


        }
        return true
    }

    private fun generateData() {
        var category = intent.getSerializableExtra("category") as Category
        setupToolbar(category)
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getSubcategory(category.catId),
            Response.Listener {

                var gson = Gson()
                var subCategoryResponse = gson.fromJson(it, SubCategoryResponse::class.java)
                subcategoryList.addAll(subCategoryResponse.data)

                var myFragmentAdapter = MyFragmentAdapter(supportFragmentManager)
                for (i in 0 until subcategoryList.size){
                    myFragmentAdapter.addFragment(subcategoryList[i])
                }

                view_pager.adapter=myFragmentAdapter
                tab_layout.setupWithViewPager(view_pager)

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