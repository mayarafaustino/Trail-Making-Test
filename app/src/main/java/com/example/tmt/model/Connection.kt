package com.example.tmt.model

class Connection (
    val from: Circle,
    val to: Circle,
    val isPathInterrupted: Boolean,
    val time: Long,
    val isSequenceCorrect: Boolean,
    val isRepeated: Boolean )
