package com.example.tmt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TMTAInformation : AppCompatActivity() {

    lateinit var btnBackInformation: Button
    lateinit var btnSampleA: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tmtainformation)

        btnBackInformation = findViewById(R.id.buttonBackInformation)
        btnSampleA = findViewById(R.id.buttonSampleA)

        btnBackInformation.setOnClickListener {

            finish()
        }





    }
}