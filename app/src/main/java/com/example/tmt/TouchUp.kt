package com.example.tmt

class TouchUp ()
{

    val startTime = System.currentTimeMillis()
    var endTime = 0L

    fun calculateDuration(): Long
    {
        return endTime - startTime
    }
}