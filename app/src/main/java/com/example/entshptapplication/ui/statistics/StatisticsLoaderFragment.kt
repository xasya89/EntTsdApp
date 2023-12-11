package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.entshptapplication.databinding.FragmentStatisticsLoaderBinding
import com.example.entshptapplication.viewmodels.LoginViewModelCreater

class StatisticsLoaderFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsLoaderBinding
    private lateinit var statisticsViewModel: StatisticsViewModel
    private var workerId: Int = 0
    private val timer = object :CountDownTimer(20_000, 4_000){
        override fun onTick(p0: Long) {
            onPause()
            statisticsViewModel.getResult()
            onStart()
        }

        override fun onFinish() {

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsLoaderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkerId()
        statisticsViewModel = StatisticsViewModelFactory.Create(this)
        statisticsViewModel.addJob(workerId)
        statisticsViewModel.summary.observe(viewLifecycleOwner, {
            if(it!=null)
                Toast.makeText(context, "Getting result", Toast.LENGTH_LONG).show()
        })
        timer.start()

    }

    fun getWorkerId(){
        val loginViewModel = LoginViewModelCreater.createViewModel(this)
        workerId = loginViewModel.worker.value?.id ?: 0;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatisticsLoaderFragment()
    }
}