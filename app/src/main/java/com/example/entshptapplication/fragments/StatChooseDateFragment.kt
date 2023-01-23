package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.StatChooseDateRecycleAdapter
import com.example.entshptapplication.databinding.FragmentStatChooseDateBinding
import com.google.gson.Gson
import org.json.JSONArray


private const val ARG_PARAM1 = "dateList"

class StatChooseDateFragment : Fragment() {

    private var dateList: List<String> = listOf()

    private lateinit var binding : FragmentStatChooseDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            val gson = Gson()
            dateList = gson.fromJson(it!!.getString(ARG_PARAM1), Array<String>::class.java).toList()
            dateList = dateList.plus("Сбросить")
        }
        /*
        arguments?.let {
            dateList = it.get(ARG_PARAM1) as List<String>
        }
         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatChooseDateBinding.inflate(inflater)
        binding.statChooseDateRecycleView.adapter = StatChooseDateRecycleAdapter(dateList, {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, StatFragment.newInstance(it))
                setReorderingAllowed(true)
            }
        })
        binding.statChooseDateRecycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(dateList: List<String>) =
            StatChooseDateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, JSONArray(dateList).toString())
                }
            }
    }
}