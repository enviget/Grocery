package com.example.grocery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activities.SubCategoryActivity
import com.example.grocery.apps.Config
import com.example.grocery.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_category_adapter.view.*

class AdapterCategory(var mContext: Context) : RecyclerView.Adapter<AdapterCategory.MyViewHolder>() {
    var mList :ArrayList<Category> = ArrayList()


    inner class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category){
            itemView.text_view_category_name.text = category.catName

            Picasso.get().load("${Config.IMAGE_URL+category.catImage}").into(itemView.image_view_category_image)

            itemView.setOnClickListener {

                var intent = Intent(mContext, SubCategoryActivity::class.java)
                intent.putExtra("category", category)
                mContext.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_category_adapter, parent,false)
        var myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])

    }

    fun setData(list:ArrayList<Category>){
        mList.addAll(list)
        notifyDataSetChanged()
    }
}