package com.example.tmt

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TMTBInformation : AppCompatActivity() {

    lateinit var btnBackHome: Button
    lateinit var btnSampleB: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_tmtbinformation)

        btnBackHome = findViewById(R.id.buttonBackHome)
        btnSampleB = findViewById(R.id.buttonSampleB)

        btnBackHome.setOnClickListener {

            finish()
        }



    }
}