package com.example.entshptapplication.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.entshptapplication.fragments.StatNaryadsFragment

class StatViewPageAdapter(fragment: FragmentActivity, private val selectedDate: String?):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        if(position==0)
            fragment = StatNaryadsFragment.newInstance("upak", selectedDate)
        if(position==1)
            fragment = StatNaryadsFragment.newInstance("shpt", selectedDate)
        return fragment!!
    }
}