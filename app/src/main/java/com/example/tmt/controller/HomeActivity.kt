package com.example.tmt.controller

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tmt.R
import com.example.tmt.model.TestSession

class HomeActivity : AppCompatActivity() {

    lateinit var btnEnter : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_home)

        btnEnter = findViewById(R.id.btn_home)

        btnEnter.setOnClickListener {

            val intent = Intent(this, InformationActivity::class.java)
            TestSession.clear()
            startActivity(intent)
        }

    }
}