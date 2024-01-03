package com.example.entshptapplication.ui.actions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.entshptapplication.ui.naryadInfo.NaryadInfoFragment
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentActionsBinding
import com.example.entshptapplication.ui.shpt.shpt_list.ActShptFragment
import com.example.entshptapplication.ui.upak.UpakListFragment
import com.example.entshptapplication.ui.login.LoginFragment
import com.example.entshptapplication.ui.statistics.StatisticsLoaderFragment
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import com.example.entshptapplication.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsFragment : Fragment() {
    private lateinit var binding: FragmentActionsBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val keyListenerViewModel by activityViewModels<KeyListenerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActionsBinding.inflate(inflater)
        binding.upakActionBtn.setOnClickListener {
            transitionToFragment(UpakListFragment.newInstance())
        }
        binding.actListActionBtn.setOnClickListener {
            transitionToFragment(ActShptFragment.newInstance())
        }
        binding.statActionBtn.setOnClickListener {
            transitionToFragment((StatisticsLoaderFragment.newInstance()))
        }

        binding.findActionBtn.setOnClickListener {
            transitionToFragment((NaryadInfoFragment.newInstance()))
        }
        binding.exitActionBtn.setOnClickListener {
            loginViewModel.logOut()
            transitionToFragment(LoginFragment.newInstance())
        }

        return binding.root
    }

    fun transitionToFragment(fragment: Fragment){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
            setReorderingAllowed(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.worker.observe(viewLifecycleOwner, {
            binding.workerFIOTextView.text = it?.fio
        })
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActionsFragment().apply {}
    }
}