package com.vandina.lungsbotkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class SplashScreen : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent (this, Login::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}