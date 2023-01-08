package com.example.entshptapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.UpakRecycleAdapter
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databinding.FragmentUpakListBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.repository.LoginRepository
import com.example.entshptapplication.viewmodels.*
import android.media.MediaPlayer
import androidx.fragment.app.commit

class UpakListFragment : Fragment() {

    private lateinit var binding: FragmentUpakListBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var upakViewModel: UpakViewModel
    private lateinit var keyListenerViewModel: KeyListenerViewModel
    private var adapter = UpakRecycleAdapter()
    private lateinit var soundPlayer: SoundPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        soundPlayer = SoundPlayer(requireContext())

        binding = FragmentUpakListBinding.inflate(inflater)
        adapter.onItemClick = {naryad -> showDialog(naryad) }
        binding.upakListRecycle.adapter = adapter
        binding.upakListRecycle.layoutManager = LinearLayoutManager(context)

        binding.upakListBtnSave.setOnClickListener {
            upakViewModel.save(loginViewModel.login.value?.id!!)
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
        binding.findManualBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, FindNaryadsFragment.newInstance(FIND_NARYAD_PARENT_UPAK))
                setReorderingAllowed(true)
            }
        }
        binding.upakListBtnCancel.setOnClickListener {
            upakViewModel.clearUpakList()
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
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

        var upakApi = UpakApi.getInstance(HOSTED_NAME)
        upakViewModel = ViewModelProvider(activity?.viewModelStore!!, UpakViewModelFactory(upakApi)).get(UpakViewModel::class.java)
        //upakViewModel = UpakViewModel(upakApi)
        upakViewModel.upakList.observe(viewLifecycleOwner, {
            adapter.setNaryads(it)
            binding.summaryCount.text = it.size.toString()
            binding.summaryCost.text = it.sumOf{ it.upakCost.toDouble() }.toString()
        })
        upakViewModel.responseError.observe(viewLifecycleOwner,{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            soundPlayer.playError()
        })

        keyListenerViewModel = ViewModelProvider(activity?.viewModelStore!!, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {
            if(it!="")
                upakViewModel.scan(it)
        })
    }


    private fun showDialog( naryad: Naryad) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.naryad_actions_dialog)
        val width = (resources.displayMetrics.widthPixels*0.97).toInt()
        val height = 350
        dialog?.window?.setLayout(width, height)
        val body = dialog?.findViewById(R.id.naryadDialogBody) as TextView
        body.text = naryad.num
        val btnRemove = dialog.findViewById<Button>(R.id.naryadDialogRemove)
        val btnInfo = dialog.findViewById<Button>(R.id.naryadDialogInfo)
        val btnClose = dialog.findViewById<Button>(R.id.naryadDialogClose)
        btnRemove.setOnClickListener {
            upakViewModel.upakList.value = upakViewModel.upakList.value?.minus(naryad) ?: listOf()
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        //val noBtn = dialog.findViewById(R.id.noBtn) as TextView
        /*
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
         */
        //noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpakListFragment().apply {}
    }
}