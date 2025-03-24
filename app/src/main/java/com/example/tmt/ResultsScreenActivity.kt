package com.example.tmt

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface

class ResultsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_results_screen)


        val btnToggleA: ImageButton = findViewById(R.id.buttonExpandA)
        val btnToggleB: ImageButton = findViewById(R.id.buttonExpandB)
        val expandableLayoutA: LinearLayout = findViewById(R.id.expandLayoutA)
        val expandableLayoutB: LinearLayout = findViewById(R.id.expandLayoutB)
        val btnBackToHome: Button = findViewById(R.id.buttonBackToHome)


        // Configura o clique do botão para expandir e recolher o LinearLayoutA
        btnToggleA.setOnClickListener {
            if (expandableLayoutA.visibility == View.GONE) {
                // Expande
                expandableLayoutA.visibility = View.VISIBLE
                btnToggleA.setImageResource(R.drawable.up_arrow)
            } else {
                // Recolhe
                expandableLayoutA.visibility = View.GONE
                btnToggleA.setImageResource(R.drawable.drop_arrow)
            }
        }

        // Configura o clique do botão para expandir e recolher o LinearLayoutB

        btnToggleB.setOnClickListener {
            if (expandableLayoutB.visibility == View.GONE) {
                // Expande
                expandableLayoutB.visibility = View.VISIBLE
                btnToggleB.setImageResource(R.drawable.up_arrow)
            } else {
                // Recolhe
                expandableLayoutB.visibility = View.GONE
                btnToggleB.setImageResource(R.drawable.drop_arrow)
            }
        }


        btnBackToHome.setOnClickListener {
            // Criar o AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmar saída")
            builder.setMessage("Você tem certeza que deseja sair?")

            builder.setPositiveButton("Sim") { dialog, which ->
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            builder.setNegativeButton("Não") { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
        }

    }

}