package com.example.entshptapplication.fragments

import android.app.AlertDialog
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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.entshptapplication.TSDApplication

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
        binding = FragmentUpakListBinding.inflate(inflater)
        adapter.onItemClick = {naryad -> showDialog(naryad) }
        binding.upakListRecycle.adapter = adapter
        binding.upakListRecycle.layoutManager = LinearLayoutManager(context)

        binding.upakListBtnSave.setOnClickListener {
            actionComplite()
        }
        binding.findManualBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, FindNaryadsFragment.newInstance(FIND_NARYAD_PARENT_UPAK))
                setReorderingAllowed(true)
            }
        }
        binding.upakListBtnCancel.setOnClickListener {
            actionClearList()
        }

        binding.upakFragmentFindTextView.addTextChangedListener {
            upakViewModel.updateFilterStr(it?.toString() ?: "")
        }

        addCloseAction()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soundPlayer = SoundPlayer(context)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            LoginRepository(LoginApi.getInstance(HOSTED_NAME))
        )
        ).get(LoginViewModel::class.java)

        upakViewModel = ViewModelProvider(activity?.viewModelStore!!, UpakViewModelFactory(
            UpakApi.getInstance(HOSTED_NAME), (requireActivity().application  as TSDApplication).upakDbRepository,
            {
                soundPlayer.playError()
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )).get(UpakViewModel::class.java)
        upakViewModel.loadFromDb()
        //upakViewModel = UpakViewModel(upakApi)
        upakViewModel.upakList.observe(viewLifecycleOwner, {
            binding.summaryCount.text = it.size.toString()
            //binding.summaryCost.text = it.sumOf{ it.upakCost.toDouble() }.toString()
        })
        upakViewModel.upakFilterList.observe(viewLifecycleOwner, {
            adapter.setNaryads(it)
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
        val height = 450
        dialog?.window?.setLayout(width, height)
        val body = dialog?.findViewById(R.id.naryadDialogBody) as TextView
        body.text = naryad.num
        val btnRemove = dialog.findViewById<Button>(R.id.naryadDialogRemove)
        val btnInfo = dialog.findViewById<Button>(R.id.naryadDialogInfo)
        val btnClose = dialog.findViewById<Button>(R.id.naryadDialogClose)
        btnRemove.setOnClickListener {
            upakViewModel.deleteNaryad(naryad)
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun actionComplite(){
        val buillder = AlertDialog.Builder(context)
            .setTitle("Выполнить")
            .setMessage("Отметить выполненным наряды в списке?")
            .setNegativeButton("нет",{dialog, i -> dialog.cancel()})
            .setPositiveButton("да",{dialog, i ->
                upakViewModel.save(loginViewModel.login.value?.id!!, {
                    upakViewModel.clearUpakList({
                        parentFragmentManager.commit {
                            replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                            setReorderingAllowed(true)
                        }
                    })
                })
            })
        buillder.show()
    }

    private fun actionClearList(){
        val buillder = AlertDialog.Builder(context)
            .setTitle("Очистить")
            .setMessage("Удалить отмеченные наряды?")
            .setNegativeButton("нет",{dialog, i -> dialog.cancel()})
            .setPositiveButton("да",{dialog, i ->
                upakViewModel.clearUpakList()
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                    setReorderingAllowed(true)
                }
            })
        buillder.show()
    }

    private fun addCloseAction(){
        binding.upakFragmentCloseBtn.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpakListFragment().apply {}
    }
}