package com.example.entshptapplication.fragments

import android.content.Context
import android.media.MediaPlayer
import com.example.entshptapplication.R

class SoundPlayer constructor(val context: Context) {
    private var mMediaPlayer: MediaPlayer? = null
    fun  playError(){
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context, R.raw.kaspersky_error)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
}