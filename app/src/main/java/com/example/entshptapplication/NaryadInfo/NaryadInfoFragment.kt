package com.example.entshptapplication.NaryadInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentNaryadInfoBinding
import com.example.entshptapplication.fragments.ActionsFragment
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import com.example.entshptapplication.viewmodels.KeyListenerViewModelFactory


class NaryadInfoFragment : Fragment() {
    private lateinit var binding: FragmentNaryadInfoBinding
    private lateinit var viewModel: NaryadInfoViewModel
    private lateinit var keyListenerViewModel: KeyListenerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNaryadInfoBinding.inflate(inflater)
        binding.btnFind.setOnClickListener {
            if(binding.findNaryadInput.text.toString()!="")
                viewModel.getByNaryadNum(binding.findNaryadInput.text.toString())
        }
        binding.btnClose.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = NaryadInfoViewModelFactory.Create(this)
        viewModel.clear()
        viewModel.findNaryad.observe(viewLifecycleOwner, {
            if(it!=null)
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainerView, NaryadInfoVeiwerFragment.newInstance())
                    setReorderingAllowed(true)
                }
        })

        keyListenerViewModel = ViewModelProvider(activity?.viewModelStore!!, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
        keyListenerViewModel.barCode.value = ""
        keyListenerViewModel.barCode.observe(viewLifecycleOwner, {
            if(it!=null && it!="")
                viewModel.getByNaryadId(it!!)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NaryadInfoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}