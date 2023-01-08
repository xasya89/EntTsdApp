package com.example.entshptapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.R
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.databinding.FragmentActionsBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.repository.LoginRepository
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import com.example.entshptapplication.viewmodels.KeyListenerViewModelFactory
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelFactory


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
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, UpakListFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        binding.actListActionBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActShptFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        binding.exitActionBtn.setOnClickListener {
            loginViewModel.logOut()
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, LoginFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginApi = LoginApi.getInstance(HOSTED_NAME)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            LoginRepository(loginApi)
        )
        ).get(LoginViewModel::class.java)
        loginViewModel.login.observe(viewLifecycleOwner, {
            binding.workerFIOTextView.text = it?.fio
        })

        keyListenerViewModel = ViewModelProvider(activity?.viewModelStore!!, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActionsFragment().apply {}
    }
}