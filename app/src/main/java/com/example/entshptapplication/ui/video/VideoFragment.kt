package com.example.entshptapplication.ui.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentVideoBinding
import com.example.entshptapplication.ui.video.adapters.VideoPlayerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : Fragment() {

    private lateinit var binding : FragmentVideoBinding
    private lateinit var adapter: VideoPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater)
        adapter  = VideoPlayerAdapter(this)
        binding.videoViewpager.adapter =adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setCams(arrayOf("cam1", "cam2", "cam3"))
        BottomSheetBehavior.from(binding.videoBottomSheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VideoFragment()
    }
}