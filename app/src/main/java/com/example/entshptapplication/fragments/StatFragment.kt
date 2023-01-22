package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.StatViewPageAdapter
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.StatApi
import com.example.entshptapplication.databinding.FragmentStatBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.repository.LoginRepository
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelFactory
import com.example.entshptapplication.viewmodels.StatViewModel
import com.example.entshptapplication.viewmodels.StatViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class StatFragment : Fragment() {
    private lateinit var binding: FragmentStatBinding
    private val statViewModel:StatViewModel by viewModels {
        StatViewModelFactory(StatApi.getInstance(HOSTED_NAME),{})
    }
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatBinding.inflate(inflater)
        val adapter = StatViewPageAdapter(requireActivity())
        binding.statViewPager.adapter=adapter
        TabLayoutMediator(binding.statTabs, binding.statViewPager){ tab, position ->
            if(position==1)
                tab.text = "Упаковка"
            else
                tab.text = "Погрузка"
        }.attach()
        binding.statTabs.addOnTabSelectedListener (object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected (tab: TabLayout.Tab) {
            }
            override fun onTabUnselected (tab: TabLayout.Tab) {
            }
            override fun onTabReselected (tab: TabLayout.Tab) {
            }
        })
        addExitListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            LoginRepository(LoginApi.getInstance(HOSTED_NAME))
        )
        ).get(LoginViewModel::class.java)

        statViewModel.getSummary(loginViewModel.login.value?.id ?: 0)
        statViewModel.summary.observe(viewLifecycleOwner,{
            binding.statChooseDate.text = it.dateWithStr
            binding.statSummaryUpakCount.text = it.upakCount.toString()
            binding.statSummaryUpakCost.text = it.upakCost.toString()
            binding.statSummaryShptCount.text = it.shptCount.toString()
            binding.statSummaryShptCost.text = it.shptCost.toString()
            binding.statSummeryPaymentPlus.text = it.paymentsPlus.toString()
            binding.statSummeryPaymentMinus.text = it.paymentsMinus.toString()
        })
    }

    fun addExitListener(){
        binding.statCloseBtn.setOnClickListener(object : OnClickListener{
            override fun onClick(p0: View?) {
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                    setReorderingAllowed(true)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatFragment().apply {}
    }
}