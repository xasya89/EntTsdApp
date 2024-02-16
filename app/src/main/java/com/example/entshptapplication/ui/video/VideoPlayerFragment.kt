package com.example.entshptapplication.ui.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentVideoPlayerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM_CAM_NAME = "cam_name"
class VideoPlayerFragment : Fragment() {
    private var paramCamName: String? = null
    private lateinit var binding: FragmentVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramCamName = it.getString(ARG_PARAM_CAM_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlayerBinding.inflate(inflater)
        binding.videoPlayerCamName.text = paramCamName
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(camName: String) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_CAM_NAME, camName)
                }
            }
    }
}