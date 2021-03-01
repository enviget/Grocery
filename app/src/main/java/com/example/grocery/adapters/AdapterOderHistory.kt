package com.example.grocery.adapters

import android.content.Context
import android.net.ParseException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.apps.Config

import com.example.grocery.models.Product

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rows_order_history_adapter.view.*
import java.text.SimpleDateFormat

class AdapterOrderHistory(var mContext: Context) : RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder>() {
    var mList :ArrayList<Product> = ArrayList()


    inner class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product){

            itemView.text_view_history_name.text = product.productName
            itemView.text_view_history_price.text = "$${product.price.toString()}"
            itemView.text_view_history_quantity.text ="#${product.quantity}"
//            itemView.text_view_history_date.text = convertMongoDate(product.date.toString())


            Picasso.get().load("${Config.IMAGE_URL+product.image}").into(itemView.image_view_history_cart)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.rows_order_history_adapter, parent,false)
        var myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])

    }

    fun setData(list: ArrayList<Product>){
        mList=list
        notifyDataSetChanged()
    }
    fun convertMongoDate(date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("MMM d, yyyy")
        try {
            val finalStr: String = outputFormat.format(inputFormat.parse(date))
            println(finalStr)
            return finalStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
}