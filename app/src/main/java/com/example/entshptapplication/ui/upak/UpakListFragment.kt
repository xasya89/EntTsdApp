package com.example.entshptapplication.ui.upak

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
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databinding.FragmentUpakListBinding
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.viewmodels.*
import android.os.CountDownTimer
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.example.entshptapplication.di.TSDApplication
import com.example.entshptapplication.dialogs.NaryadActionBottomSheetDialog
import com.example.entshptapplication.fragments.CustomDialogs
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.ui.actions.ActionsFragment
import com.example.entshptapplication.ui.findNaryads.FIND_NARYAD_PARENT_UPAK
import com.example.entshptapplication.ui.findNaryads.FindNaryadsFragment
import com.example.entshptapplication.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpakListFragment : Fragment() {

    private lateinit var binding: FragmentUpakListBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val upakViewModel by activityViewModels<UpakViewModel>()
    private val keyListenerViewModel by activityViewModels<KeyListenerViewModel>()
    private var adapter = UpakRecycleAdapter()

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
                replace(R.id.fragmentContainerView, FindNaryadsFragment.newInstance(
                    FIND_NARYAD_PARENT_UPAK
                ))
                setReorderingAllowed(true)
            }
        }
        binding.upakListBtnCancel.setOnClickListener {
            actionClearList()
        }

        binding.upakFragmentFindTextView.addTextChangedListener {
            upakViewModel.search( it?.toString() ?: "")
        }

        addCloseAction()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upakViewModel.loadFromDb()
        upakViewModel.error.observe(viewLifecycleOwner, Observer {
            if(it!=null)
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        upakViewModel.upakList.observe(viewLifecycleOwner, {
            binding.summaryCount.text = it.size.toString()
            adapter.setNaryads(it)
        })

        upakViewModel.scanResult.observe(viewLifecycleOwner, Observer {
            Log.d("error",it)
        })

        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {
            if(it!="")
                upakViewModel.scan(it)
        })
    }


    private fun showDialog( naryad: Naryad) {
        val bootomDialog = NaryadActionBottomSheetDialog(naryad,{
            upakViewModel.findNaryadGet(naryad.id, {
                CustomDialogs.showInfoDialog(context, it)
            })
        }, { upakViewModel.deleteNaryad(naryad) })
        bootomDialog.show(parentFragmentManager, NaryadActionBottomSheetDialog.TAG)
    }

    private fun actionComplite(){
        val buillder = AlertDialog.Builder(context)
            .setTitle("Выполнить")
            .setMessage("Отправить наряды на сервер?")
            .setNegativeButton("нет",{dialog, i -> dialog.cancel()})
            .setPositiveButton("да",{dialog, i ->
                upakViewModel.save(loginViewModel.worker.value?.id!!, {
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