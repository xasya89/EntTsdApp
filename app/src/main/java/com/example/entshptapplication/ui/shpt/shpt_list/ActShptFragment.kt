package com.example.entshptapplication.ui.shpt.shpt_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentActShptBinding
import com.example.entshptapplication.ui.shpt.shpt_one.ShptOneFragment
import com.example.entshptapplication.models.ActShpt
import com.example.entshptapplication.ui.actions.ActionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActShptFragment : Fragment() {
    private lateinit var binding: FragmentActShptBinding
    private val shptViewModel by viewModels<ShptViewModel>()
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
        shptViewModel.getActList().observe(viewLifecycleOwner, Observer {
            binding.actShptLoadingAnimation.visibility = View.GONE
            binding.actListRecycleView.visibility = View.VISIBLE
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