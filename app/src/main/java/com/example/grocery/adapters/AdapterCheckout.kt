package com.example.grocery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activities.CheckOutCartActivity
import com.example.grocery.apps.Config
import com.example.grocery.helpers.DBHelper
import com.example.grocery.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rows_checkout_adapter.view.*

class AdapterCheckout(var mContext: Context) :
    RecyclerView.Adapter<AdapterCheckout.MyViewHolder>() {


    var mList = ArrayList<Product>()
    var dbhelper = DBHelper(mContext)

    var listener: View.OnClickListener = mContext as CheckOutCartActivity

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product) {

            itemView.text_view_cart_mrp.text = "$ ${product.mrp}"
            itemView.text_view_cart_name.text = product.productName
            itemView.text_view_cart_price.text = "$ ${product.price}"
            itemView.text_view_cart_quantity.text = product.quantity.toString()
            Picasso.get().load(Config.IMAGE_URL + product.image).into(itemView.image_view_cart)

            itemView.button_cart_delete.setOnClickListener {
                dbhelper.deleteItem(product)
                mList.remove(product)
                listener?.onClick(itemView)
                notifyDataSetChanged()

            }

            itemView.button_cart_add.setOnClickListener {
                var quantity = dbhelper.getItemQuantity(product)
                product.quantity = quantity + 1
                dbhelper.updateQuantity(product)
                itemView.text_view_cart_quantity.text = dbhelper.getItemQuantity(product).toString()
                listener?.onClick(itemView)
                notifyDataSetChanged()
            }

            itemView.button_cart_minus.setOnClickListener {
                var quantity = dbhelper.getItemQuantity(product)
                if (quantity - 1 != 0) {
                    product.quantity = quantity - 1
                }
                dbhelper.updateQuantity(product)
                itemView.text_view_cart_quantity.text = dbhelper.getItemQuantity(product).toString()
                listener?.onClick(itemView)
                notifyDataSetChanged()
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.rows_checkout_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun setData(list: ArrayList<Product>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }


}