package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.commit
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentStatisticsBinding
import com.example.entshptapplication.fragments.ActionsFragment
import com.google.android.material.tabs.TabLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var creatorViewModel: StatisticsCreatorViewModel
    private lateinit var statisticsViewModel: StatisticsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        creatorViewModel = StatisticsCreatorViewModelFactory.Create(this)
        val summary = creatorViewModel.summary.value
        statisticsViewModel = StatisticsViewModelFactory.Create(this)
        statisticsViewModel.setSummary(summary!!)
        setSummaryValuesInTextView()
        loadNaryads()
    }

    private fun setSummaryValuesInTextView() = with(binding){
        val summary = statisticsViewModel.getSummaryOnDay()
        statisticsLastActSum.text = summary.lastActSum.toString()
        statisticsPaymentsSum.text = summary.paymentsSumAll.toString()
        statisticsUpakSum.text = summary.upakSumAll.toString()
        statisticsShptSum.text = summary.shptSumAll.toString()
        val dateFormat = SimpleDateFormat("dd.MM.YY")

        statisticsSelectedDateTv.text =
            (if(statisticsViewModel.selectedDay.value==null) "от " else "") +
            dateFormat.format( statisticsViewModel.selectedDay.value ?: statisticsViewModel.dates.value!!.min())
    }

    private fun  loadNaryads(){
        statisticsViewModel.getNaryads()

    }

    private fun initBinding() = with(binding) {
        statisticsChooseDateBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, StatisticsChooseDateFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        statisticsChooseDateCloseBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        statisticsTabView.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("Select tab", tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatisticsFragment()
    }
}