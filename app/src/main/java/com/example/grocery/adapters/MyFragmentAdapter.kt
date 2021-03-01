package com.example.grocery.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.grocery.fragments.SubCategoryFragment
import com.example.grocery.models.SubCategory

class MyFragmentAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm){
    var mFragment : ArrayList<Fragment> = ArrayList()
    var mTitle : ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

    override fun getCount(): Int {
        return mTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitle[position]
    }

    fun addFragment(subcategory : SubCategory){
        mTitle.add(subcategory.subName)
        mFragment.add(SubCategoryFragment.newInstance(subcategory.subId))
    }

}