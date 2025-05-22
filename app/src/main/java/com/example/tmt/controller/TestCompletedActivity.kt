package com.example.tmt.controller

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tmt.R

class TestCompletedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_test_completed)

        val btnResult: Button = findViewById(R.id.buttonGoToResults)

        btnResult.setOnClickListener {

            val intent = Intent(this, ResultsScreenActivity::class.java)

            startActivity(intent)
        }


    }
}