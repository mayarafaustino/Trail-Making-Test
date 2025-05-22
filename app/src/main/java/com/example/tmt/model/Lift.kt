package com.example.tmt.model

class Lift ()
{

    val startTime = System.currentTimeMillis()
    var endTime = 0L

    fun calculateDuration(): Long
    {
        return endTime - startTime
    }
}