package com.example.tmt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    lateinit var btnEnter : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        btnEnter = findViewById(R.id.btn_home)

        btnEnter.setOnClickListener {

            val intent = Intent(this, InformationActivity::class.java)

            startActivity(intent)
        }

    }
}