package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.StatViewPageAdapter
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.NewStatApi
import com.example.entshptapplication.communications.StatApi
import com.example.entshptapplication.databinding.FragmentStatBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelFactory
import com.example.entshptapplication.viewmodels.StatViewModel
import com.example.entshptapplication.viewmodels.StatViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONArray

private const val ARG_CHOOSE_DATE = "ARG_CHOOSE_DATE"

class StatFragment : Fragment() {
    private lateinit var binding: FragmentStatBinding
    private val statViewModel:StatViewModel by activityViewModels<StatViewModel> {
        StatViewModelFactory(NewStatApi.getInstance(HOSTED_NAME))
    }
    private lateinit var loginViewModel: LoginViewModel
    private var dateList: MutableList<String> = mutableListOf()
    private var filterDateChoose: String? = null
    private var workerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            filterDateChoose = it?.getString(ARG_CHOOSE_DATE) ?: null
            if(filterDateChoose=="Сбросить")
                filterDateChoose = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatBinding.inflate(inflater)
        val adapter = StatViewPageAdapter(requireActivity(), filterDateChoose)
        binding.statViewPager.adapter=adapter
        TabLayoutMediator(binding.statTabs, binding.statViewPager){ tab, position ->
            if(position==1)
                tab.text = "Погрузка"
            else
                tab.text = "Упаковка"
        }.attach()
        binding.statTabs.addOnTabSelectedListener (object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected (tab: TabLayout.Tab) {
                getSummaryByDate()
            }
            override fun onTabUnselected (tab: TabLayout.Tab) {
            }
            override fun onTabReselected (tab: TabLayout.Tab) {
            }
        })

        addExitListener()
        addChooseDateListener()
        binding.statFilterEdit.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                adapter.onUpakFilter?.invoke(binding.statFilterEdit.text.toString())
                adapter.onShptFilter?.invoke(binding.statFilterEdit.text.toString())
                return true
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkerId()
        statViewModel.getSummary(workerId)
        getSummaryByDate()
        statViewModel.summary.observe(viewLifecycleOwner,{
            binding.statChooseDate.text = if(filterDateChoose==null) { "с " + it.dateWithStr } else "на " + filterDateChoose
            binding.statSummeryPaymentPlus.text = it.paymentsPlus.toString()
            binding.statSummeryPaymentMinus.text = it.paymentsMinus.toString()
            binding.statSummaryUpakCount.text = it.upakCount.toString()
            binding.statSummaryUpakCost.text = it.upakCost.toString()
            binding.statSummaryShptCount.text = it.shptCount.toString()
            binding.statSummaryShptCost.text = it.shptCost.toString()

            binding.statSummeryAmount.text = (it.paymentsMinus + it.paymentsPlus + it.upakCost + it.shptCost).toString()

            dateList.clear()
            dateList.addAll(it.shptCompliteDateListStr)
            for(dateOne in it.upakCompliteDateListStr)
                if(dateList.indexOf(dateOne)==-1)
                    dateList.add(dateOne)
        })
        statViewModel.error.observe(viewLifecycleOwner, Observer {
            if(it!=null)
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
        })
    }

    fun getWorkerId(){
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            LoginApi.getInstance(HOSTED_NAME)
        )
        ).get(LoginViewModel::class.java)
        workerId = loginViewModel.login.value?.id ?: 0;
    }

    fun getSummaryByDate(){
        statViewModel.getSummary(workerId, filterDateChoose)
    }

    //Кнопка выхода
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

    //Выбор даты
    fun addChooseDateListener(){
        binding.statChooseDateBtn.setOnClickListener { chooseDate() }
        binding.statChooseDate.setOnClickListener { chooseDate() }
    }

    fun  chooseDate(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, StatChooseDateFragment.newInstance(dateList))
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(dateChoose: String? = null) = StatFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CHOOSE_DATE, dateChoose)
            }
        }
    }
}