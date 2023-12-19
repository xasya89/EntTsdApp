package com.example.entshptapplication.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.entshptapplication.databinding.DialogConfirmBinding

class GenericConfirmDialog(private val context: Context, private val layoutInflater: LayoutInflater, private val title: String, private val onSuccess: () -> Unit, private val onCancel: (()->Unit)? = null)  {

    fun show(){
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogConfirmBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        dialogBinding.confirmTitle.text = title
        dialogBinding.confirmSuccess.setOnClickListener {
            onSuccess.invoke()
            alertDialog.dismiss()
        }
        dialogBinding.confirmCancel.setOnClickListener {
            onCancel?.invoke()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}