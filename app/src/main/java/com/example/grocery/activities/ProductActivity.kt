package com.example.grocery.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import com.example.grocery.R
import com.example.grocery.apps.Config
import com.example.grocery.helpers.DBHelper
import com.example.grocery.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

class ProductActivity : AppCompatActivity(), View.OnClickListener {
    private var textViewCartCount: TextView? = null
    lateinit var dbhelper: DBHelper
    lateinit var product: Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        dbhelper = DBHelper(this)
        product = intent.getSerializableExtra(Product.KEY_PRODUCT) as Product
        init()
    }

    private fun init() {

        setupToolbar()
        updateCartCount()
        text_view_product_description.text = product.description
        text_view_product_name.text = product.productName
        text_view_product_price.text = "$${product.price}"
        text_view_product_mrp.text = "$${product.mrp}"
        Picasso.get().load("${Config.IMAGE_URL + product.image}").into(image_view_product_image)
        updateUI()

        button_addtocart.setOnClickListener(this)
        button_cart_minus.setOnClickListener(this)
        button_cart_add.setOnClickListener(this)


    }
    private fun setupToolbar(){
        toolbar.title = "Detail"
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

    fun updateUI() {
        var quantity = dbhelper.getItemQuantity(product)
        if (quantity > 0) {
            button_addtocart.visibility = View.GONE
            button_group.visibility = View.VISIBLE
            text_view_cart_quantity.text = quantity.toString()
        } else {
            button_group.visibility = View.GONE
            button_addtocart.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        when (view) {
            button_addtocart -> dbhelper.addItem(product)
            button_cart_add -> {
                var quantity = dbhelper.getItemQuantity(product)
                product.quantity = quantity + 1
                dbhelper.updateQuantity(product)
            }
            button_cart_minus -> {
                var quantity = dbhelper.getItemQuantity(product)
                if (quantity -1 != 0)
                product.quantity = quantity - 1
                dbhelper.updateQuantity(product)
            }
        }
        updateUI()
        updateCartCount()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        updateCartCount()

    }

}