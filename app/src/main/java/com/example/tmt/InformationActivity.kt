package com.example.tmt

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class InformationActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    lateinit var patientName : EditText
    lateinit var patientAge: EditText
    lateinit var patientIdentifier: EditText
    lateinit var professionalName : EditText
    lateinit var professionalIdentifier : EditText
    lateinit var btnConfirm : Button
    lateinit var btnBackMain : Button
    //val viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_information)

        patientName = findViewById(R.id.inputPatientName)
        patientAge = findViewById(R.id.inputPatientAge)
        professionalName = findViewById(R.id.inputProfessionalName)
        patientIdentifier = findViewById(R.id.inputPatientIdentifier)
        professionalIdentifier= findViewById(R.id.inputProfessionalIdentifier)
        btnConfirm = findViewById(R.id.buttonConfirm)
        btnBackMain = findViewById(R.id.buttonBackMain)

        btnConfirm.setOnClickListener {
            val intent = Intent(this, TMTAInformationActivity::class.java)


            if (patientName.text.toString().isEmpty()) {
                patientName.error = "Nome do paciente é obrigatório"
            } else if (patientAge.text.toString().isEmpty()) {
                patientAge.error = "Idade é obrigatória"
            } else if (professionalName.text.toString().isEmpty()) {
                professionalName.error = "Nome do aplicador é obrigatório"
            } else {

                sharedViewModel.patientName = patientName.text.toString()
                sharedViewModel.patientAge = patientAge.text.toString().toIntOrNull()
                sharedViewModel.patientIdentifier = patientIdentifier.text.toString()
                sharedViewModel.professionalName = professionalName.text.toString()
                sharedViewModel.professionalIdentifier = professionalIdentifier.text.toString()

                startActivity(intent)
            }


        }

        btnBackMain.setOnClickListener {
            finish()
        }
    }
}