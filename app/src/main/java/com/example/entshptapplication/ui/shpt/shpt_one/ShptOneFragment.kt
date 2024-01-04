package com.example.entshptapplication.ui.shpt.shpt_one

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.MainActivity
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentShptOneBinding
import com.example.entshptapplication.dialogs.NaryadActionBottomSheetDialog
import com.example.entshptapplication.fragments.CustomDialogs
import com.example.entshptapplication.ui.findNaryads.FIND_NARYAD_PARENT_SHPT
import com.example.entshptapplication.ui.findNaryads.FindNaryadsFragment
import com.example.entshptapplication.models.ActShptDoor
import com.example.entshptapplication.ui.actions.ActionsFragment
import com.example.entshptapplication.ui.login.LoginViewModel
import com.example.entshptapplication.ui.findNaryads.FindNaryadsViewModel
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM_ID_ACT = "idAct"

@AndroidEntryPoint
class ShptOneFragment : Fragment() {
    private var idAct: Int? = null
    private lateinit var binding: FragmentShptOneBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val shptOneViewModel by activityViewModels<ShptOneViewModel>()
    private val keyListenerViewModel by activityViewModels<KeyListenerViewModel>()
    private val findNaryadsViewModel by activityViewModels<FindNaryadsViewModel>()
    private var adapter = ShptOneRecycleAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAct = it.getInt(ARG_PARAM_ID_ACT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShptOneBinding.inflate(inflater)

        binding.shptOneRecycleView.adapter = adapter
        binding.shptOneRecycleView.layoutManager = LinearLayoutManager(context)
        adapter.onActionClick = {actDoor ->
            openActionsDialog(actDoor)
        }
        binding.shptOneCloseBtn.setOnClickListener { close() }
        binding.shptOneSearchTextView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null)
                    search(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText ?: "" == "")
                    search("")
                return false
            }
        })
        binding.shptOneSearchBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.fragmentContainerView,
                    FindNaryadsFragment.newInstance(FIND_NARYAD_PARENT_SHPT, idAct)
                )
                setReorderingAllowed(true)
            }
        }
        binding.shptOneCompliteBtn.setOnClickListener {
            shptOneViewModel.complite(idAct!!, loginViewModel.worker.value!!.id, {
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                    setReorderingAllowed(true)
                }
            })
        }
        binding.shptOneCancelCompliteBtn.setOnClickListener {
            shptOneViewModel.cancelComplite(idAct!!,{
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                    setReorderingAllowed(true)
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        loadAct()

        shptOneViewModel.naryads.observe(viewLifecycleOwner, {
            binding.shptOneDoorCountTextView.text = it.size.toString()
            adapter.setDoors(it)
            if(it.none { naryad -> naryad.isInDb }==true)
                binding.shptOneCompliteCardView.visibility = View.GONE
            else
                binding.shptOneCompliteCardView.visibility = View.VISIBLE
        })
        shptOneViewModel.error.observe(viewLifecycleOwner, {
            if(it!=null)
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
        })
    }

    private fun loadAct(){
        shptOneViewModel.getOActOne(idAct!!).observe(viewLifecycleOwner, {act->
            binding.shptOneActTextView.text = act?.actNum.toString() + " - " + act?.actDateStr
            binding.shptOneCarNumTextView.text = act?.carNum
            binding.shptOneFahrerTextView.text = act?.fahrer
        })
    }

    fun initViewModels(){
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner,{
            if(it!="")
                shptOneViewModel.scan(idAct!!, it, loginViewModel.worker.value!!.id)
        })
        clearBarCode()
        findNaryadsViewModel.naryad.value = null
        findNaryadsViewModel.naryad.observe(viewLifecycleOwner, {
            if(it!=null)
                CustomDialogs.showInfoDialog(context, it)
        })
    }

    fun openActionsDialog(actDoor: ActShptDoor){
        val dialog = NaryadActionBottomSheetDialog(
            actDoor.num,
            { shptOneViewModel.delete(idAct!!, actDoor, loginViewModel.worker.value?.id!!) },
            { findNaryadsViewModel.get(actDoor.idNaryad) }
        )
        dialog.show(parentFragmentManager, NaryadActionBottomSheetDialog.TAG)
    }

    fun close(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    fun search(str: String){
        shptOneViewModel.search(idAct!!, str)
    }

    fun clearBarCode(){
        (activity as MainActivity).clearBarCode()
    }

    companion object {
        @JvmStatic
        fun newInstance(idAct: Int) =
            ShptOneFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_ID_ACT, idAct)
                }
            }
    }
}