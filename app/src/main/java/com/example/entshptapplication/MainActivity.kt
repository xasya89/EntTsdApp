package com.example.entshptapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.entshptapplication.viewmodels.KeyListenerViewModel
import com.example.entshptapplication.viewmodels.KeyListenerViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var keyListenerViewModel: KeyListenerViewModel
    private var barCode: String = ""
    fun clearBarCode(){
        barCode = ""
        keyListenerViewModel.barCode.value = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyListenerViewModel = ViewModelProvider(this, KeyListenerViewModelFactory()).get(KeyListenerViewModel::class.java)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.action==KeyEvent.ACTION_DOWN){
            when (event?.keyCode){
                KeyEvent.KEYCODE_0 -> barCode = barCode + "0"
                KeyEvent.KEYCODE_1 -> barCode = barCode + "1"
                KeyEvent.KEYCODE_2 -> barCode = barCode + "2"
                KeyEvent.KEYCODE_3 -> barCode = barCode + "3"
                KeyEvent.KEYCODE_4 -> barCode = barCode + "4"
                KeyEvent.KEYCODE_5 -> barCode = barCode + "5"
                KeyEvent.KEYCODE_6 -> barCode = barCode + "6"
                KeyEvent.KEYCODE_7 -> barCode = barCode + "7"
                KeyEvent.KEYCODE_8 -> barCode = barCode + "8"
                KeyEvent.KEYCODE_9 -> barCode = barCode + "9"
                KeyEvent.KEYCODE_N -> barCode = barCode + "N"
                KeyEvent.KEYCODE_E -> barCode = barCode + "E"
                KeyEvent.KEYCODE_ENTER -> {
                    keyListenerViewModel.barCode.value = barCode
                    barCode = ""
                    return false
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}