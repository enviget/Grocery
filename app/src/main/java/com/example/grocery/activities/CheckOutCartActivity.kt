package com.example.grocery.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.R
import com.example.grocery.adapters.AdapterCheckout
import com.example.grocery.helpers.DBHelper
import com.example.grocery.helpers.SessionManager
import com.example.grocery.models.PromoCode
import kotlinx.android.synthetic.main.activity_check_out_cart.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*

open class CheckOutCartActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_cart)

        sessionManager = SessionManager(this)

        init()
    }

    private fun init() {
        setupToolbar()
        var dbhelper = DBHelper(this)
        var adapterCheckout = AdapterCheckout(this)
        adapterCheckout.setData(dbhelper.readData())
        recycler_view_check_out.adapter = adapterCheckout
        recycler_view_check_out.layoutManager = LinearLayoutManager(this)

        updateTotal()

        button_checkout_checkout.setOnClickListener {

            updateTotal()
            if (sessionManager.login()) {
                if (dbhelper.getTotalQuantity() != 0) {
                    startActivity(Intent(this, AddressActivity::class.java))

                } else
                    Toast.makeText(applicationContext, "Cart is Empty", Toast.LENGTH_LONG).show()
            } else {
                dialogLogin()
            }
        }

        button_check_out_apply.setOnClickListener {
            updateTotal()

        }
    }

    override fun onClick(view: View?) {
        updateTotal()
    }

    private fun dialogLogin() {
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to login?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                startActivity(Intent(this@CheckOutCartActivity, LoginActivity::class.java))
            }

        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                dialog?.dismiss()
            }
        })
        builder.create().show()
    }

    private fun setupToolbar() {
        toolbar.title = "Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item = menu?.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        view.text_view_cart_count.visibility = View.GONE
        view.image_view_icon_cart.visibility = View.GONE

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_homepage -> startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

        return true
    }

    fun updateTotal() {
        var dbhelper = DBHelper(this)
        var inputPromo = edit_text_check_out_promo_code.text.toString()

        var total = (dbhelper.getTotal() * PromoCode.addPromo(inputPromo)).toInt()
        var mrp = dbhelper.getMRP().toInt()
        var discount = (dbhelper.getMRP() - (dbhelper.getTotal() * PromoCode.addPromo(inputPromo))).toInt()

        var sharedPreferences = getSharedPreferences(sessionManager.FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(sessionManager.KEY_TOTAL, total).commit()
        sharedPreferences.edit().putInt(sessionManager.KEY_DISCOUNT, discount).commit()

        text_view_check_out_price.text = "Total: $$total"
        text_view_check_out_mrp.text = "Subtotal : $$mrp"
        text_view_check_out_discount.text = "Discount : $$discount"

        progressBar_checkout_cart.visibility = View.GONE

    }
}