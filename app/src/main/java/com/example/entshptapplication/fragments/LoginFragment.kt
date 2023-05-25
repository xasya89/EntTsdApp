package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.entshptapplication.MainActivity
import com.example.entshptapplication.R
import com.example.entshptapplication.TSDApplication
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.dao.AppDatabase
import com.example.entshptapplication.databinding.FragmentLoginBinding
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.LoginModel
import com.example.entshptapplication.models.Worker
import com.example.entshptapplication.viewmodels.LoginViewModel
import com.example.entshptapplication.viewmodels.LoginViewModelFactory
import com.example.entshptapplication.viewmodels.SettingsViewModel
import com.example.entshptapplication.viewmodels.SettingsViewModelFactory
import kotlinx.coroutines.flow.collect

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory((requireActivity().application  as TSDApplication).settingsDbRepository)
    }

    private var worker: Worker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)


        binding.settingsBtn.setOnClickListener {
            openSettingsFrament()
        }

        binding.loginBtn.setOnClickListener {
            loginViewModel.getLogin(LoginModel(smartCartNum = binding.smartCartEdit.text.toString()))
                .observe(viewLifecycleOwner, Observer {
                    if(it?.id == 0)
                        return@Observer
                    worker=it
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                        setReorderingAllowed(true)
                    }
                })
        }

        return  binding.root
    }
    @Suppress
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.setting.observe(viewLifecycleOwner, {
            if(it==null){
                settingsViewModel.insert(ConnectionSetting(ServerHost = "192.168.1.200"))
                binding.settingsBtn.text = "http://192.168.1.200:5226/"
                HOSTED_NAME = "http://192.168.1.200:5226/"
            } else {
                binding.settingsBtn.text = it.ServerHost.toString()
                HOSTED_NAME = "http://"+it.ServerHost.toString()+":5226/"
            }
            try{
                val loginApi = LoginApi.getInstance(HOSTED_NAME)
                loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
                    loginApi
                )).get(LoginViewModel::class.java)
                /*
                loginViewModel.login.observe(viewLifecycleOwner, Observer {
                    if(it.id == 0)
                        return@Observer
                    worker=it
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                        setReorderingAllowed(true)
                    }
                })
                 */
            }
            catch (ex: Exception){
                Log.e("login api error",ex.message.toString())
                openSettingsFrament()
            }
        })

        clearBarCode()
    }

    fun clearBarCode(){
        (activity as MainActivity).clearBarCode()
    }

    fun openSettingsFrament(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, SettingsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {}
    }
}