package com.example.entshptapplication.ui.statistics

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentStatisticsLoaderBinding
import com.example.entshptapplication.fragments.ActionsFragment
import com.example.entshptapplication.viewmodels.LoginViewModelCreater

class StatisticsLoaderFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsLoaderBinding
    private lateinit var creatorViewModle: StatisticsCreatorViewModel
    private var workerId: Int = 0
    private val timer = object :CountDownTimer(20_000, 4_000){
        override fun onTick(p0: Long) {
            onPause()
            creatorViewModle.getResult()
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
        binding.statisticsLoaderBtnCancel.setOnClickListener {
            timer.onFinish()
            timer.cancel()
            creatorViewModle.cancelJob()
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkerId()
        creatorViewModle = StatisticsCreatorViewModelFactory.Create(this)
        creatorViewModle.addJob(workerId)
        creatorViewModle.summary.observe(viewLifecycleOwner, {
            if(it!=null){
                timer.cancel()
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, StatisticsFragment.newInstance())
                    setReorderingAllowed(true)
                }
            }
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