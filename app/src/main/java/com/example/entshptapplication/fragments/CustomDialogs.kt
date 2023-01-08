package com.example.entshptapplication.fragments

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import com.example.entshptapplication.R
import com.example.entshptapplication.communications.FindNaryadsApi
import com.example.entshptapplication.databinding.NaryadInfoDialogBinding
import com.example.entshptapplication.models.FindNaryadModel

object CustomDialogs {
    fun showInfoDialog(context: Context?, naryadModel: FindNaryadModel?){
        if(context==null || naryadModel==null)
            return
        val dialog = context!!.let { Dialog(it) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.naryad_info_dialog)
        val width = (context.resources.displayMetrics.widthPixels).toInt()
        val height = (context.resources.displayMetrics.heightPixels).toInt()
        dialog.window?.setLayout(width, height)

        dialog.findViewById<ImageButton>(R.id.dialogInfoCloseBtn).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<TextView>(R.id.dialogInfoNaryadNum).text = naryadModel.naryadNum
        dialog.findViewById<TextView>(R.id.dialogInfoShet).text = naryadModel.shet + " - " + naryadModel.shetDateStr
        dialog.findViewById<TextView>(R.id.dialogInfoDoorName).text = naryadModel.doorName
        dialog.findViewById<TextView>(R.id.dialogInfoOpen).text = naryadModel.open
        dialog.findViewById<TextView>(R.id.dialogInfoRal).text = naryadModel.ral
        dialog.findViewById<TextView>(R.id.dialogInfoDovod).text = naryadModel.dovod
        dialog.findViewById<TextView>(R.id.dialogInfoNalichnik).text = naryadModel.nalichnik
        dialog.findViewById<TextView>(R.id.dialogInfoShtild).text = naryadModel.shtild
        dialog.findViewById<TextView>(R.id.dialogInfoNote).text = naryadModel.note

        dialog.findViewById<TextView>(R.id.dialogInfoSvarkaFio).text = naryadModel.svarkaCompliteFio
        dialog.findViewById<TextView>(R.id.dialogInfoSvarkaCost).text = naryadModel.svarkaCompliteDateStr

        dialog.findViewById<TextView>(R.id.dialogInfoSborkaFio).text = naryadModel.sborkaCompliteFio
        dialog.findViewById<TextView>(R.id.dialogInfoSborkaCost).text = naryadModel.sborkaCompliteDateStr

        dialog.findViewById<TextView>(R.id.dialogInfoColorFio).text = naryadModel.colorCompliteFio
        dialog.findViewById<TextView>(R.id.dialogInfoColorCost).text = naryadModel.colorCompliteDateStr

        dialog.findViewById<TextView>(R.id.dialogInfoUpakFio).text = naryadModel.upakCompliteFio
        dialog.findViewById<TextView>(R.id.dialogInfoUpakCost).text = naryadModel.upakCompliteDateStr

        dialog.findViewById<TextView>(R.id.dialogInfoShptFio).text = naryadModel.shptCompliteFio
        dialog.findViewById<TextView>(R.id.dialogInfoShptCost).text = naryadModel.shptCompliteDateStr
        dialog.show()


    }
}