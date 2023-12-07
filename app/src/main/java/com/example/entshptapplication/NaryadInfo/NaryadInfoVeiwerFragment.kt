package com.example.entshptapplication.NaryadInfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.entshptapplication.NaryadInfo.Models.NaryadInfoModel
import com.example.entshptapplication.R
import com.example.entshptapplication.databinding.FragmentNaryadInfoVeiwerBinding
import com.example.entshptapplication.fragments.ActionsFragment

class NaryadInfoVeiwerFragment : Fragment() {
    private lateinit var naryadInfoViewModel: NaryadInfoViewModel
    private lateinit var binding: FragmentNaryadInfoVeiwerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNaryadInfoVeiwerBinding.inflate(inflater)
        binding.closeBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragmentContainerView, ActionsFragment.newInstance())
                setReorderingAllowed(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        naryadInfoViewModel = NaryadInfoViewModelFactory.Create(this)
        naryadInfoViewModel.findNaryad.observe(viewLifecycleOwner, {
            val naryadInfo = it!!
            binding.infoShetAndDate.text = "Дверь: " + naryadInfo.shet + "/" + naryadInfo.numInOrder
            binding.naryadInfoNaryadNum.text = "Наряд: " + naryadInfo.naryadNum
            binding.naryadInfoShet.text = naryadInfo.shet + " от " + naryadInfo.shetDateStr
            binding.naryadInfoDoor.text = naryadInfo.doorName + " " + naryadInfo.doorSize
            binding.naryadInfoOpen.text = naryadInfo.open
            binding.naryadInfoRal.text = naryadInfo.ral
            binding.naryadInfoDovod.text = naryadInfo.dovod
            binding.naryadInfoNalichnik.text = naryadInfo.nalichnik
            binding.naryadInfoShild.text = naryadInfo.shtild
            binding.naryadInfoNote.text = naryadInfo.note
            binding.naryadInfoSvarka.text = (naryadInfo.svarkaFio ?: "" ) + " " + (naryadInfo.svarkaDateCompliteStr ?: "")
            binding.naryadInfoSborka.text = (naryadInfo.sborkaFio ?: "") + " " + (naryadInfo.sborkaDateCompliteStr ?: "")
            binding.naryadInfoColor.text = (naryadInfo.colorFio ?: "") + " " + (naryadInfo.colorDateCompliteStr ?: "")
            binding.naryadInfoUpak.text = (naryadInfo.upakFio ?: "") + " " + (naryadInfo.upakDateCompliteStr ?: "")
            binding.naryadInfoShpt.text = (naryadInfo.shptFio ?: "") + " " + (naryadInfo.shptDateCompliteStr ?: "")
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = NaryadInfoVeiwerFragment()
    }
}