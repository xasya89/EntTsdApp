package com.example.entshptapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.adapters.ShptRecycleAdapter
import com.example.entshptapplication.communications.ShptApi
import com.example.entshptapplication.databinding.FragmentActShptBinding
import com.example.entshptapplication.models.ActShpt
import com.example.entshptapplication.models.HOSTED_NAME
import com.example.entshptapplication.viewmodels.ShptViewModel
import com.example.entshptapplication.viewmodels.ShptViewModelFactory

class ActShptFragment : Fragment() {
    private lateinit var binding: FragmentActShptBinding
    private lateinit var shptViewModel: ShptViewModel
    private var adapter = ShptRecycleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActShptBinding.inflate(inflater)

        binding.actListRecycleView.adapter = adapter
        adapter.onOpenAct = {actShpt ->  openAct(actShpt)}
        binding.actListRecycleView.layoutManager = LinearLayoutManager(context)
        binding.btnBack.setOnClickListener { back() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var shptApi = ShptApi.getInstance(HOSTED_NAME)
        shptViewModel = ViewModelProvider(activity?.viewModelStore!!, ShptViewModelFactory(shptApi,{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })).get(ShptViewModel::class.java)

        shptViewModel.getActList().observe(viewLifecycleOwner, Observer {

            adapter.setList(it)
        })
    }

    fun back(){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
            setReorderingAllowed(true)
        }
    }

    fun openAct(actShpt: ActShpt){
        parentFragmentManager.commit {
            replace(R.id.fragmentContainerView, ShptOneFragment.newInstance(actShpt.idAct))
            setReorderingAllowed(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ActShptFragment().apply {}
    }
}