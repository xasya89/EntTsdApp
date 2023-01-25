package com.example.entshptapplication.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.entshptapplication.R

class ConfirmDialog(private val title: String, private val message: String, private val iconId: Int, private val  onConfirm: (()->Unit)): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMessage(message)
                .setIcon(iconId)
                .setPositiveButton("да") {dialog, id ->
                    onConfirm.invoke()
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}