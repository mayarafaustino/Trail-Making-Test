package com.example.tmt
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var patientName: String? = null
    var patientAge: Int? = null
    var patientIdentifier: String? = null
    var professionalName: String? = null
    var professionalIdentifier: String? = null
    var imageTMTA: String? = null
    var imageTMTB: String? = null
}