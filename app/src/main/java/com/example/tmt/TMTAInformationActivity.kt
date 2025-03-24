package com.example.tmt

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TMTAInformationActivity : AppCompatActivity() {

    lateinit var btnBackInformation: Button
    lateinit var btnSampleA: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_tmtainformation)

        btnBackInformation = findViewById(R.id.buttonBackInformation)
        btnSampleA = findViewById(R.id.buttonSampleA)

        btnBackInformation.setOnClickListener {

            finish()
        }

        btnSampleA.setOnClickListener {

            val intent = Intent(this, TMTBInformationActivity::class.java)

            startActivity(intent)
        }





    }
}