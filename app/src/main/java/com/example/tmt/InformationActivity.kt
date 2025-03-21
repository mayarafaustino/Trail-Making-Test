package com.example.tmt

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InformationActivity : AppCompatActivity() {

    lateinit var patientName : EditText
    lateinit var patientAge: EditText
    lateinit var profissionalName : EditText
    lateinit var btnConfirm : Button
    lateinit var btnBackMain : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_information)

        patientName = findViewById(R.id.inputPatientName)
        patientAge = findViewById(R.id.inputPatientAge)
        profissionalName = findViewById(R.id.inputProfessionalName)
        btnConfirm = findViewById(R.id.buttonConfirm)
        btnBackMain = findViewById(R.id.buttonBackMain)

        btnConfirm.setOnClickListener {
            if (patientName.text.toString().isEmpty()) {
                patientName.error = "Nome do paciente é obrigatório"
            } else if (patientAge.text.toString().isEmpty()) {
                patientAge.error = "Idade é obrigatória"
            } else if (profissionalName.text.toString().isEmpty()) {
                profissionalName.error = "Nome do aplicador é obrigatório"
            }
        }

        btnBackMain.setOnClickListener {
            finish()
        }
    }
}