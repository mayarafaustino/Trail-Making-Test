package com.example.tmt.model

object TestSession
{
    // Dados do paciente
    var patientName: String? = null
    var patientAge: Int? = null
    var patientIdentifier: String? = null

    // Dados do profissional
    var professionalName: String? = null
    var professionalIdentifier: String? = null

    // Imagens dos testes
    var imageTMTA: String? = null
    var imageTMTB: String? = null

    // Resultados dos testes
    val results = mutableListOf<TestResults>()

    fun clear() {
        patientName = null
        patientAge = null
        patientIdentifier = null
        professionalName = null
        professionalIdentifier = null
        imageTMTA = null
        imageTMTB = null
        results.clear()
    }
}