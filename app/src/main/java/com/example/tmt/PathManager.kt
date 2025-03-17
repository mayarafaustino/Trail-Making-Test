package com.example.tmt

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

class PathManager {

    var isDrawing = false
    var currentPath = Path()
    val paths = mutableListOf<Path>()
    val pathPoints = mutableListOf<Pair<Float, Float>>()

    val pathPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    fun startPath(x: Float, y: Float) {
        currentPath = Path().apply { moveTo(x, y) }
        pathPoints.clear()
        pathPoints.add(Pair(x, y))
        isDrawing = true
    }

    fun updatePath(x: Float, y: Float) {
        currentPath.lineTo(x, y)
        pathPoints.add(Pair(x, y))
    }

    fun finishPath() {
        paths.add(currentPath)
        isDrawing = false
    }
}