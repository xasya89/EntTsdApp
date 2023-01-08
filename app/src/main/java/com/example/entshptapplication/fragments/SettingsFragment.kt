package com.example.entshptapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.entshptapplication.R
import com.example.entshptapplication.R.*
import com.example.entshptapplication.TSDApplication
import com.example.entshptapplication.dao.AppDatabase
import com.example.entshptapplication.dao.SettingsDao
import com.example.entshptapplication.databinding.FragmentLoginBinding
import com.example.entshptapplication.databinding.FragmentSettingsBinding
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.viewmodels.SettingsViewModel
import com.example.entshptapplication.viewmodels.SettingsViewModelFactory

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory((requireActivity().application  as TSDApplication).settingsDbRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.settingSaveBtn.setOnClickListener{

            settingsViewModel.clear()
            settingsViewModel.insert(ConnectionSetting(ServerHost = binding.serverHostSettingEdit.text.toString()))
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, LoginFragment.newInstance())
                setReorderingAllowed(true)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val defaulHost = "192.168.1.200"
        settingsViewModel.setting.observe(viewLifecycleOwner,{
            if(it==null){
                settingsViewModel.insert(ConnectionSetting(ServerHost = defaulHost))
                binding.serverHostSettingEdit.setText(defaulHost)
            } else {
                binding.serverHostSettingEdit.setText(it.ServerHost)
            }

        }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply { }
    }
}