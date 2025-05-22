package com.example.tmt.model

import android.graphics.Color
import android.graphics.Paint

class Circle( val x: Float, val y: Float, val text: String, val ordem: Int )
{
    val radius = 50f

    val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    val strokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
}