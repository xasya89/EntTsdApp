package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.StatRecycleAdapter
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.StatApi
import com.example.entshptapplication.databinding.FragmentStatNaryadsBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.repository.LoginRepository
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelFactory
import com.example.entshptapplication.viewmodels.StatViewModel
import com.example.entshptapplication.viewmodels.StatViewModelFactory

private const val STAT_NARYAD_ARG_PARAMETER = "STAT_TYPE"
private const val ARG_SELECTED_DATE = "SELECTED_DATE"

class StatNaryadsFragment : Fragment() {

    private var statType: String = ""
    private var selectedDate: String? = null
    private lateinit var binding: FragmentStatNaryadsBinding
    private lateinit var loginViewModel: LoginViewModel
    private val statViewModel: StatViewModel by viewModels {
        StatViewModelFactory(StatApi.getInstance(HOSTED_NAME),{})
    }
    private var adapter = StatRecycleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            statType = it.getString(STAT_NARYAD_ARG_PARAMETER) ?: "upak"
            selectedDate = it.getString(ARG_SELECTED_DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatNaryadsBinding.inflate(inflater)
        binding.statRecycleView.adapter = adapter
        binding.statRecycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            LoginRepository(LoginApi.getInstance(HOSTED_NAME))
        )
        ).get(LoginViewModel::class.java)

        if(statType=="upak") {
            statViewModel.upakNaryadList.observe(viewLifecycleOwner, {
                adapter.setNaryads(it)
            })
            statViewModel.getUpakList(loginViewModel.login.value!!.id, null, selectedDate, 0, 50)
        }
        if(statType=="shpt"){
            statViewModel.shptNaryadList.observe(viewLifecycleOwner,{
                adapter.setNaryads(it)
            })
            statViewModel.getShptList(loginViewModel.login.value!!.id, null, selectedDate, 0, 50)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.textView12.text = statType + (0..100).random().toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(statType: String, selectedDate: String?) =
            StatNaryadsFragment().apply {
                arguments = Bundle().apply {
                    putString(STAT_NARYAD_ARG_PARAMETER, statType)
                    putString(ARG_SELECTED_DATE, selectedDate)
                }
            }
    }
}