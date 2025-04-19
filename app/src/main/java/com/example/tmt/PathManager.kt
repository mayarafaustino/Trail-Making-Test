package com.example.tmt

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

class PathManager {

    var isDrawing = false
    var currentPath = Path()
    val paths = mutableListOf<Path>()
    val pathPoints = mutableListOf<Pair<Float, Float>>()
    val pathStartTimes = mutableListOf<Long>()
    val touchUpList = mutableListOf<TouchUp>()

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
        recordStartTime()
    }

    fun updatePath(x: Float, y: Float) {
        currentPath.lineTo(x, y)
        pathPoints.add(Pair(x, y))
    }

    fun finishPath() {
        paths.add(currentPath)
        isDrawing = false
        recordTouchUp()
    }

    //Adiciona tempo de início de cada path e fim de cada toque levantado
    private fun recordStartTime()
    {
        val time = System.currentTimeMillis()
        pathStartTimes.add(time)
        if(touchUpList.isNotEmpty())
            touchUpList.last().endTime = time

    }

    private fun recordTouchUp()
    {
        touchUpList.add(TouchUp())
    }
}