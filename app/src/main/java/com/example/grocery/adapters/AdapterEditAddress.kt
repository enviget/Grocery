package com.example.grocery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.activities.UpdateAddressActivity
import com.example.grocery.models.Address
import kotlinx.android.synthetic.main.rows_address_adapter.view.*

class AdapterEditAddress(var mContext:Context) : RecyclerView.Adapter<AdapterEditAddress.MyViewHolder>()  {

    var mAddress = ArrayList<Address>()


    inner class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(address: Address){
            itemView.text_view_address_street_name.text = address.streetName
            itemView.text_view_address_city.text = address.city
            itemView.text_view_address_houseNo.text = address.houseNo
            itemView.text_view_address_pincode.text = address.pincode.toString()
            itemView.text_view_address_type.text = address.type

            itemView.setOnClickListener {
                var intent = Intent(mContext, UpdateAddressActivity::class.java)
                intent.putExtra(Address.KEY_ADDRESS, address)
                mContext.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.rows_address_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mAddress.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mAddress[position])
    }

    fun setData(list: ArrayList<Address>){
        mAddress = list
        notifyDataSetChanged()
    }


}