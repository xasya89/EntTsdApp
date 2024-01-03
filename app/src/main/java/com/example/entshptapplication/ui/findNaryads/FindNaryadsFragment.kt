package com.example.entshptapplication.ui.findNaryads

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentFindNaryadsBinding
import com.example.entshptapplication.ui.upak.UpakListFragment
import com.example.entshptapplication.ui.login.LoginViewModel
import com.example.entshptapplication.ui.shpt.shpt_one.ShptOneFragment
import com.example.entshptapplication.ui.shpt.shpt_one.ShptOneViewModel
import com.example.entshptapplication.ui.upak.UpakViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val ARG_PARAM1 = "type-screen"
private const val ARG_PARAM_ID_ACT = "idAct"
const val FIND_NARYAD_PARENT_UPAK = "upak"
const val FIND_NARYAD_PARENT_SHPT = "shpt"

@AndroidEntryPoint
class FindNaryadsFragment : Fragment() {
    private var paramTypeParentScreen: String? = null
    private var actShptId: Int? = null
    private lateinit var binding: FragmentFindNaryadsBinding

    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val upakViewModel by activityViewModels<UpakViewModel>()
    private val shptOneViewModel by activityViewModels<ShptOneViewModel>()
    private val findNaryadsViewModel by activityViewModels<FindNaryadsViewModel>()
    private lateinit var adapter: FindNaryadsRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramTypeParentScreen = it.getString(ARG_PARAM1)
            val act = it.getInt(ARG_PARAM_ID_ACT)
            if(act!=0)
                actShptId=act
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindNaryadsBinding.inflate(inflater)

        adapter = FindNaryadsRecycleAdapter()
        binding.findRecycleView.adapter = adapter
        binding.findRecycleView.layoutManager=LinearLayoutManager(context)
        adapter.onItemCheck= {findNaryad ->
            when(paramTypeParentScreen){
                FIND_NARYAD_PARENT_UPAK -> {
                    if(findNaryad.upakComplite and !findNaryad.onChecked){
                        Toast.makeText(context, findNaryad.naryadNum + " уже выполнен", Toast.LENGTH_SHORT).show()
                        findNaryad.onChecked = false
                    }
                    if(findNaryad.upakComplite==false)
                        findNaryad.onChecked = !findNaryad.onChecked
                }
                FIND_NARYAD_PARENT_SHPT -> {
                    if(findNaryad.shptComplite and !findNaryad.onChecked){
                        findNaryad.onChecked = false
                    }
                    if(findNaryad.shptComplite==false)
                        findNaryad.onChecked = !findNaryad.onChecked
                }
            }
            findNaryad.onChecked
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNaryadsViewModel.clear()
        //Debounce для поиска нарядов
        var searchFor = ""
        binding.findTextView.addTextChangedListener{
            val searchText = it.toString().trim()
            if (searchText == searchFor)
                return@addTextChangedListener
            searchFor = searchText
            lifecycleScope.launch {
                delay(500)  //debounce timeOut
                if (searchText != searchFor)
                    return@launch
                findNaryadsViewModel.find(binding.findTextView.text.toString())
            }
        }
        binding.btnSave.setOnClickListener { saveCheckedNaryads() }
        binding.btnCancel.setOnClickListener { cancelClick() }
        findNaryadsViewModel.findNaryadList.observe(viewLifecycleOwner,{
            adapter.setNaryads(it)
        })
    }

    fun saveCheckedNaryads(){
        val naryads = findNaryadsViewModel.findNaryadList.value?.filter { it.onChecked } ?: listOf()
        when(paramTypeParentScreen){
            FIND_NARYAD_PARENT_UPAK -> {
                upakViewModel.addFindCheckedNaryads(naryads, {
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainerView, UpakListFragment.newInstance())
                        setReorderingAllowed(true)
                    }
                })
            }
            FIND_NARYAD_PARENT_SHPT -> {
                var idNaryads = mutableListOf<Int>()
                for (naryad in naryads)
                    idNaryads.add(naryad.id)
                shptOneViewModel.chooseList(actId = actShptId!!, idNaryads, loginViewModel.worker.value?.id ?: 0, {
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainerView, ShptOneFragment.newInstance(actShptId!!))
                        setReorderingAllowed(true)
                    }
                })
            }
        }
    }

    fun cancelClick(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView,
                if(paramTypeParentScreen== FIND_NARYAD_PARENT_SHPT)
                    ShptOneFragment.newInstance(actShptId!!)
                else
                    UpakListFragment.newInstance()
            )
            setReorderingAllowed(true)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(typeParentScreen: String, actId: Int? = null) =
            FindNaryadsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, typeParentScreen)
                    putInt(ARG_PARAM_ID_ACT, actId ?: 0)
                }
            }
    }
}