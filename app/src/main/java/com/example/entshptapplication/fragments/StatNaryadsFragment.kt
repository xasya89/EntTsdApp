package com.example.entshptapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.StatRecycleAdapter
import com.example.entshptapplication.databinding.FragmentStatNaryadsBinding

private const val STAT_NARYAD_ARG_PARAMETER = "STAT_TYPE"
private const val STAT_NARYAD_ARG_PARAMETER_UPAK = "upak"
private const val STAT_NARYAD_ARG_PARAMETER_SHPT = "shpt"

class StatNaryadsFragment : Fragment() {

    private var statType: String = ""
    private lateinit var binding: FragmentStatNaryadsBinding
    private var adapter = StatRecycleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            statType = it.getString(STAT_NARYAD_ARG_PARAMETER) ?: "upak"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatNaryadsBinding.inflate(inflater)

        binding.statRecycleView.adapter = adapter
        binding.statRecycleView.layoutManager = LinearLayoutManager(context)

        return inflater.inflate(R.layout.fragment_stat_naryads, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(statType: String) =
            StatNaryadsFragment().apply {
                arguments = Bundle().apply {
                    putString(STAT_NARYAD_ARG_PARAMETER, statType)
                }
            }
    }
}