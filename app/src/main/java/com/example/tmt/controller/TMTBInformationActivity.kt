package com.example.tmt.controller

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tmt.R
import com.example.tmt.model.TmtType

class TMTBInformationActivity : AppCompatActivity() {

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

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        btnSampleB.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("TMT_TYPE", TmtType.TMT_B_SAMPLE.name)
            startActivity(intent)
        }



    }
}