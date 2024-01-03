package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentStatisticsChooseDateBinding
import com.example.entshptapplication.ui.statistics.adapters.StatisticsChooseDateAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class StatisticsChooseDateFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsChooseDateBinding
    private val statisticsViewModel by activityViewModels<StatisticsViewModel>()
    private val adapter = StatisticsChooseDateAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsChooseDateBinding.inflate(inflater)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setDays(statisticsViewModel.dates.value!!.toList())
        adapter.onItemClick = { setSelectedDayOrCancel(it) }
    }

    private fun init(){
        binding.apply {
            statisticsChooseDateRc.layoutManager = LinearLayoutManager(context)
            statisticsChooseDateRc.adapter = adapter
            statisticsChooseDateCloseBtn.setOnClickListener { setSelectedDayOrCancel(null) }
        }
    }

    private fun setSelectedDayOrCancel(date: Date?){
        statisticsViewModel.setSelectDay(date)
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, StatisticsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatisticsChooseDateFragment()
    }
}