package com.example.grocery.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.grocery.R
import com.example.grocery.adapters.AdapterSubCategory
import com.example.grocery.apps.Endpoint
import com.example.grocery.models.Product
import com.example.grocery.models.ProductResponse
import com.example.grocery.models.SubCategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_sub_category.view.*

private const val KEY_SUBID= "subId"

class SubCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var subId: Int = 0
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subId = it.getInt(KEY_SUBID)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_sub_category, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        var requestQueue = Volley.newRequestQueue(activity)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getProduct(subId),
            Response.Listener {

                var gson = Gson()
                var productResponse = gson.fromJson(it, ProductResponse::class.java)
                productList.addAll(productResponse.data)
                var adapterSubCategory = AdapterSubCategory(activity!!)
                adapterSubCategory.setData(productList)
                view.recycler_view2.adapter = adapterSubCategory
                view.recycler_view2.layoutManager = LinearLayoutManager(activity)

            },
            Response.ErrorListener {  }
        )
        requestQueue.add(request)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(subId: Int) =
            SubCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(SubCategory.SUBID, subId)
                }
            }
    }
}