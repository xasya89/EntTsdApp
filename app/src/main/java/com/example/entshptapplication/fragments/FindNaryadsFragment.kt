package com.example.entshptapplication.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.TSDApplication
import com.example.entshptapplication.adapters.FindNaryadsRecycleAdapter
import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.communications.LoginApi
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.communications.UpakApi
import com.example.entshptapplication.databinding.FragmentFindNaryadsBinding
import com.example.entshptapplication.models.FindNaryadModel
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.models.Naryad
import com.example.entshptapplication.viewmodels.*
import kotlinx.coroutines.*
import kotlin.math.log

private const val ARG_PARAM1 = "type-screen"
private const val ARG_PARAM_ID_ACT = "idAct"
const val FIND_NARYAD_PARENT_UPAK = "upak"
const val FIND_NARYAD_PARENT_SHPT = "shpt"

class FindNaryadsFragment : Fragment() {
    private var paramTypeParentScreen: String? = null
    private var actShptId: Int? = null
    private lateinit var binding: FragmentFindNaryadsBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var upakViewModel: UpakViewModel
    private lateinit var shptOneViewModel: ShptOneViewModel
    private lateinit var findNaryadsViewModel: FindNaryadsViewModel
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
        val loginApi = LoginApi.getInstance(HOSTED_NAME)
        loginViewModel = ViewModelProvider(activity?.viewModelStore!!, LoginViewModelFactory(loginApi)).get(LoginViewModel::class.java)

        upakViewModel = ViewModelProvider(activity?.viewModelStore!!, UpakViewModelFactory(
            UpakApi.getInstance(HOSTED_NAME), (requireActivity().application  as TSDApplication).upakDbRepository,
            {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )).get(UpakViewModel::class.java)

        val shptApi = ShptApi.getInstance(HOSTED_NAME)
        shptOneViewModel = ViewModelProvider(activity?.viewModelStore!!, ShptOneViewModelFactory(
            shptApi, (requireActivity().application  as TSDApplication).shptDbRepository
        )).get(ShptOneViewModel::class.java)

        val findNaryadsApi = FindNaryadsApi.getInstance(HOSTED_NAME)
        findNaryadsViewModel = FindNaryadsViewModel(findNaryadsApi)
        findNaryadsViewModel.findNaryadList.observe(viewLifecycleOwner,{
            adapter.setNaryads(it)
        })

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
            binding.findTextView.addTextChangedListener {
                object : TextWatcher {
                    private var searchFor = ""
                    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val searchText = s.toString().trim()
                        Log.d("launch", "------")
                        if (searchText == searchFor)
                            return
                        searchFor = searchText
                        Log.d("launch", "------")
                        lifecycleScope.launch {
                            Log.d("launch", "------")
                            delay(300)  //debounce timeOut
                            if (searchText != searchFor)
                                return@launch

                            findNaryadsViewModel.find(binding.findTextView.text.toString())
                        }

                    }

                    override fun afterTextChanged(p0: Editable?) {}
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                }
            }


        binding.btnSave.setOnClickListener { saveCheckedNaryads() }

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, UpakListFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
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
                shptOneViewModel.chooseList(actId = actShptId!!, idNaryads, loginViewModel.login.value?.id ?: 0, {
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainerView, ShptOneFragment.newInstance(actShptId!!))
                        setReorderingAllowed(true)
                    }
                })
            }
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