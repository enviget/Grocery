package com.example.grocery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activities.ProductActivity
import com.example.grocery.apps.Config
import com.example.grocery.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rows_subcategory_adapter.view.*

class AdapterSubCategory (var mContext: Context) : RecyclerView.Adapter<AdapterSubCategory.MyViewHolder>() {

    var mProduct = ArrayList<Product>()

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product){
            Picasso.get().load("${Config.IMAGE_URL + product.image}").into(itemView.image_view_subcategory_image)
            itemView.text_view_subcategory_name.text = product.productName


            itemView.setOnClickListener {

                var intent = Intent(mContext, ProductActivity::class.java)
                intent.putExtra(Product.KEY_PRODUCT, product)
                mContext.startActivity(intent)

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.rows_subcategory_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mProduct.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mProduct[position])
    }

    fun setData(list : ArrayList<Product>){
        mProduct.addAll(list)
        notifyDataSetChanged()
    }
}