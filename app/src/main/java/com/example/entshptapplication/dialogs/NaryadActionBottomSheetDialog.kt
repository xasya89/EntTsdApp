package com.example.entshptapplication.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.entshptapplication.databinding.NaryadActionsDialogBinding
import com.example.entshptapplication.models.Naryad
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NaryadActionBottomSheetDialog(
    private val title: String,
    private val onInfo: ()->Unit,
    private val onDelete: ()->Unit
): BottomSheetDialogFragment() {

    private lateinit var binding: NaryadActionsDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NaryadActionsDialogBinding.inflate(inflater)
        binding.naryadDialogBody.text = title
        binding.naryadDialogClose.setOnClickListener { dismiss() }
        binding.naryadDialogInfo.setOnClickListener {
            dismiss()
            onInfo.invoke()
        }
        binding.naryadDialogRemove.setOnClickListener {
            dismiss()
            onDelete.invoke()
        }
        return binding.root
    }

    companion object {
        const val TAG = "NaryadActionBottomSheetDialog"
    }
}