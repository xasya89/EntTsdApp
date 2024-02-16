package com.example.entshptapplication.ui.video.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.entshptapplication.ui.video.VideoPlayerFragment

class VideoPlayerAdapter(private val fragment: Fragment): FragmentStateAdapter(fragment) {
    private var cams = arrayOf<String>()

    fun setCams(_cams: Array<String>){
        cams = _cams
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int  {
        return  cams.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = VideoPlayerFragment.newInstance(cams.get(position))
        return fragment
    }
}