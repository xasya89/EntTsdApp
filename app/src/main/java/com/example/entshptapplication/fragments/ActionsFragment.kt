package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.NaryadInfo.NaryadInfoFragment
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentActionsBinding
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import com.example.entshptapplication.viewmodels.KeyListenerViewModelFactory
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelCreater


class ActionsFragment : Fragment() {
    private lateinit var binding: FragmentActionsBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var keyListenerViewModel: KeyListenerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            transitionToFragment(StatFragment.newInstance())
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
        loginViewModel = LoginViewModelCreater.createViewModel(this)
        loginViewModel.worker.observe(viewLifecycleOwner, {
            binding.workerFIOTextView.text = it?.fio
        })

        keyListenerViewModel = ViewModelProvider(activity?.viewModelStore!!, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {

        })
    }

    fun getDisplayResolution(): String{
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        return width.toString() + " x " + height.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActionsFragment().apply {}
    }
}