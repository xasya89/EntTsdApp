package com.example.entshptapplication.fragments

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.MainActivity
import com.example.entshptapplication.R
import com.example.entshptapplication.TSDApplication
import com.example.entshptapplication.adapters.ShptOneRecycleAdapter
import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.databinding.FragmentShptOneBinding
import com.example.entshptapplication.models.ActShptDoor
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.viewmodels.*

private const val ARG_PARAM_ID_ACT = "idAct"

class ShptOneFragment : Fragment() {
    private var idAct: Int? = null
    private lateinit var binding: FragmentShptOneBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var shptOneViewModel: ShptOneViewModel
    private lateinit var keyListenerViewModel: KeyListenerViewModel
    private lateinit var findNaryadsViewModel: FindNaryadsViewModel
    private var adapter = ShptOneRecycleAdapter()
    private lateinit var soundPlayer: SoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idAct = it.getInt(ARG_PARAM_ID_ACT)
        }
        soundPlayer = SoundPlayer(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShptOneBinding.inflate(inflater)

        binding.shptOneRecycleView.adapter = adapter
        binding.shptOneRecycleView.layoutManager = LinearLayoutManager(context)
        adapter.onActionClick = {actDoor -> openActionsDialog(actDoor)}
        binding.shptOneCloseBtn.setOnClickListener { close() }
        //binding.shptOneSearchTextView.inputType = InputType.TYPE_NULL
        binding.shptOneSearchTextView.addTextChangedListener  {
            search(it?.toString() ?: "")
        }
        binding.shptOneSearchBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, FindNaryadsFragment.newInstance(FIND_NARYAD_PARENT_SHPT, idAct))
                setReorderingAllowed(true)
            }
        }
        binding.shptOneCompliteBtn.setOnClickListener {
            shptOneViewModel.complite(idAct!!, loginViewModel.login.value!!.id, {
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

        shptOneViewModel.act.observe(viewLifecycleOwner, {
            if(it==null)
                return@observe
            binding.shptOneActTextView.text = it.actNum.toString() + " - " + it.actDateStr
            binding.shptOneCarNumTextView.text = it.carNum
            binding.shptOneFahrerTextView.text = it.fahrer
        })
        shptOneViewModel.naryads.observe(viewLifecycleOwner, {
            binding.shptOneDoorCountTextView.text = it.size.toString()
            adapter.setDoors(it)
        })
        shptOneViewModel.naryadsInDb.observe(viewLifecycleOwner, {
            if(it.size==0)
                binding.shptOneCompliteCardView.visibility = View.GONE
            else
                binding.shptOneCompliteCardView.visibility = View.VISIBLE
        })
        shptOneViewModel.getOActOne(idAct!!, "")
    }

    fun initViewModels(){
        val loginApi = LoginApi.getInstance(HOSTED_NAME)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(
            loginApi
        )
        ).get(LoginViewModel::class.java)

        val shptApi = ShptApi.getInstance(HOSTED_NAME)
        shptOneViewModel = ViewModelProvider(activity?.viewModelStore!!, ShptOneViewModelFactory(
            shptApi, (requireActivity().application  as TSDApplication).shptDbRepository,
            {message->
                soundPlayer.playError()
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        )).get(ShptOneViewModel::class.java)

        keyListenerViewModel = ViewModelProvider(activity?.viewModelStore!!, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner,{
            if(it!="")
                shptOneViewModel.scan(idAct!!, it, loginViewModel.login.value!!.id)
        })
        clearBarCode()

        val findNaryadsApi = FindNaryadsApi.getInstance(HOSTED_NAME)
        findNaryadsViewModel = FindNaryadsViewModel(findNaryadsApi)
        findNaryadsViewModel.naryad.observe(viewLifecycleOwner, {
            if(it!=null)
                CustomDialogs.showInfoDialog(context, it)
        })
    }

    fun openActionsDialog(actDoor: ActShptDoor){
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.shpt_action_dialog)
        val width = (resources.displayMetrics.widthPixels*0.97).toInt()
        val height = 900
        dialog?.window?.setLayout(width, height)
        val naryadNumTV = dialog?.findViewById<TextView>(R.id.shptActionsDialogNaryadNum)
        val btnInfo = dialog?.findViewById<Button>(R.id.shptActionsDialogInfo)
        val btnClose = dialog?.findViewById<Button>(R.id.shptActionsDialogClose)
        val btnDelete = dialog?.findViewById<Button>(R.id.shptActionsDialogDelete)
        naryadNumTV?.text = actDoor.num
        btnDelete?.setOnClickListener {
            shptOneViewModel.delete(idAct!!, actDoor, loginViewModel.login.value?.id!!)
            dialog.dismiss()
        }
        btnClose?.setOnClickListener { dialog.dismiss() }
        btnInfo?.setOnClickListener {
            dialog.dismiss()
            findNaryadsViewModel.get(actDoor.idNaryad)
        }
        dialog?.show()
    }

    fun close(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    fun search(str: String){
        shptOneViewModel.getOActOne(idAct!!, str)
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