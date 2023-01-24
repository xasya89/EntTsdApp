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
import androidx.recyclerview.widget.RecyclerView
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.OnLoadMoreListener
import com.example.entshptapplication.adapters.RecyclerViewLoadMoreScroll
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

    var onNaryadFilter: ((String) -> Unit)? = null
    private var isLoading = false
    private var statType: String = ""
    private var selectedDate: String? = null
    private lateinit var binding: FragmentStatNaryadsBinding
    private lateinit var loginViewModel: LoginViewModel
    private val statViewModel: StatViewModel by viewModels {
        StatViewModelFactory(StatApi.getInstance(HOSTED_NAME),{})
    }
    private var adapter = StatRecycleAdapter()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

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
        mLayoutManager = LinearLayoutManager(context)
        binding.statRecycleView.layoutManager = mLayoutManager
        binding.statRecycleView.setHasFixedSize(true)
        setRVScrollListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(
            activity?.viewModelStore!!, LoginViewModelFactory(
                LoginRepository(LoginApi.getInstance(HOSTED_NAME))
            )
        ).get(LoginViewModel::class.java)

        if (statType == "upak")
            statViewModel.upakNaryadList.observe(viewLifecycleOwner, {
                if (isLoading == true)
                    adapter.appendNaryads(it)
                else
                    adapter.setNaryads(it)
                isLoading = false
            })
        if (statType == "shpt")
            statViewModel.shptNaryadList.observe(viewLifecycleOwner, {
                if (isLoading == true)
                    adapter.appendNaryads(it)
                else
                    adapter.setNaryads(it)
                isLoading = false
            })
        sendRequest()

        onNaryadFilter = {
            startPosition = 0
            endPosition = 50
            filterString = if (it == "") null else it
            sendRequest()
        }
    }

    private  fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(context)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreNaryads()
            }
        })
        binding.statRecycleView.addOnScrollListener(scrollListener)
    }

    private var startPosition = 0
    private var endPosition = 50
    private var filterString: String? = null
    private fun loadMoreNaryads(){
        startPosition += 50
        endPosition += 50
        adapter.addLoading()
        isLoading = true
        sendRequest()
    }

    private fun sendRequest(){
        if(statType=="upak")
            statViewModel.getUpakList(loginViewModel.login.value!!.id, filterString, selectedDate, startPosition, endPosition)
        if(statType=="shpt")
            statViewModel.getShptList(loginViewModel.login.value!!.id, filterString, selectedDate, startPosition, endPosition)
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