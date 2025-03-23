package com.example.tmt

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt
import java.util.concurrent.TimeUnit

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val circleManager = CircleManager()
    val circlesA = circleManager.circlesTmtA

    private val pathManager = PathManager()
    val paths = pathManager.paths

    private var touchUpCount = 0 //Contador de toque levantado
    private var lastCircleTouchedIndex = -1

    private val startPathTimes = mutableListOf<Long>() //tempo de início de cada path
    private val startTouchUpTimes = mutableListOf<Long>() //Tempo entre pontos
    private val circleTimes = mutableListOf<Long>() //Tempo entre círculos

    private var leftTheLastCircle = false
    private val connections = mutableListOf<Connection>()
    private val circlesTouchedIndex = mutableListOf<Int>()
    private var isTestFinished = false

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        //Desenha os círculos
        circlesA.forEachIndexed { index, circle ->
            canvas.drawCircle(circle.x, circle.y, circle.radius, circle.paint)
            canvas.drawCircle(circle.x, circle.y, circle.radius, circle.strokePaint)
            canvas.drawText(circle.text, circle.x, circle.y + 15, circle.textPaint)

            if(index == 0){
                canvas.drawText("Início", circle.x, circle.y + circle.radius + circle.textPaint.textSize + 15, circle.textPaint)
            }

            if(index == circlesA.lastIndex){
                canvas.drawText("Fim", circle.x, circle.y + circle.radius + circle.textPaint.textSize + 15, circle.textPaint)
            }
        }

        //Desenha os caminhos
        paths.forEach { path ->
            canvas.drawPath(path, pathManager.pathPaint)
        }
        if (pathManager.isDrawing) {
            canvas.drawPath(pathManager.currentPath, pathManager.pathPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val x = event.x
        val y = event.y

        when (event.action)
        {
            MotionEvent.ACTION_DOWN -> {
                if (!pathManager.isDrawing)
                {
                    pathManager.startPath(x, y)
                    val startPathTime = System.currentTimeMillis()
                    startPathTimes.add(startPathTime) //Adiciona tempo de início de cada path
                }
                else
                {
                    pathManager.updatePath(x, y)
                }
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (pathManager.isDrawing && !isTestFinished)
                {
                    pathManager.updatePath(x, y)
                    checkConnection()
                    checkEndTest()
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                pathManager.finishPath()
                touchUpCount++
                val startTouchUpTime = System.currentTimeMillis()
                startTouchUpTimes.add(startTouchUpTime) //Adiciona tempo de início de cada touchUp
                Log.d("TouchUp", "Toque levantado: $touchUpCount")
            }
        }
        return true
    }

    private fun calculateDistance( point: Pair<Float, Float>, circle: Circle ): Float
    {
        return sqrt((point.first - circle.x) * (point.first - circle.x) +
                (point.second - circle.y) * (point.second - circle.y))
    }

    private fun pointIsWithinCircle( point: Pair<Float, Float>, circle: Circle ): Boolean
    {
        val distance = calculateDistance(point, circle)
        return distance <= circle.radius
    }

    private fun checkEndTest()
    {
        if( connections.isEmpty())
            return

        val isLastCircleTouched = connections.last().circleDestination.ordem == circlesA.lastIndex

        if ( isLastCircleTouched && isAllCirclesTouched(circlesA))
            finishTest()
    }

    private fun isAllCirclesTouched( circles: MutableList<Circle>) : Boolean
    {
        for (i in 0 until circles.size)
        {
            val isNotTouched = !circlesTouchedIndex.contains(i)
            if (isNotTouched)
                return false
        }

        return true
    }

    //retorna se algum path toca nos dois círculos
    private fun checkPathBetweenCircles(circle1: Circle, circle2: Circle): Boolean
    {
        var circle1Touched = false
        var circle2Touched = false

        for (point in pathManager.pathPoints)
        {
            if (pointIsWithinCircle(point, circle1))
                circle1Touched = true
            if (pointIsWithinCircle(point, circle2))
                circle2Touched = true
        }

        return circle1Touched && circle2Touched
    }

    private fun verifyCurrentCircleIndex(circles: MutableList<Circle>): Int
    {
        var currentCircleIndex = -1

        for(i in 0 until circles.size)
        {
            if( !pointIsWithinCircle( pathManager.pathPoints.last(), circles[i] ) )
            {
                if( i == lastCircleTouchedIndex )
                    leftTheLastCircle = true
                continue
            }
            currentCircleIndex = i
        }
        return currentCircleIndex
    }

    private fun checkConnection()
    {
        val currentCircleIndex = verifyCurrentCircleIndex(circlesA)
        if( currentCircleIndex == -1 )
            return

        val isFirstCircle = lastCircleTouchedIndex == -1
        val nextCorrectCircleIndex = if( isFirstCircle ) 0 else lastCircleTouchedIndex + 1
        val isSequenceCorrect = currentCircleIndex == nextCorrectCircleIndex

        if( isFirstCircle )
        {
            circlesTouchedIndex.add(currentCircleIndex)
            circleTimes.add(System.currentTimeMillis())
        }

        if( !isFirstCircle && leftTheLastCircle )
            doConnection(currentCircleIndex, isSequenceCorrect)

        lastCircleTouchedIndex = currentCircleIndex
        leftTheLastCircle = false

    }

    private fun doConnection(currentCircleIndex: Int, isSequenceCorrect: Boolean)
    {
        //temporario
        Log.d(
            "Connection",
            "Círculo ${circlesA[lastCircleTouchedIndex].text} conectado ao círculo ${circlesA[currentCircleIndex].text}"
        )
        //temporario

        circlesTouchedIndex.add(currentCircleIndex)
        circleTimes.add(System.currentTimeMillis())

        val isPathInterrupted = !checkPathBetweenCircles( circlesA[lastCircleTouchedIndex], circlesA[currentCircleIndex] )
        val connection = Connection(circlesA[lastCircleTouchedIndex],
            circlesA[currentCircleIndex],
            isPathInterrupted,
            timeBetweenLastConnection(),
            isSequenceCorrect)
        connections.add(connection)

    }

    //retorna o tempo em segundos entre o ultimo e penultimo circulo conectado
    private fun timeBetweenLastConnection(): Long
    {
        val connectionTime = circleTimes[circleTimes.size - 1] - circleTimes[circleTimes.size - 2]
//        return TimeUnit.MILLISECONDS.toSeconds(connectionTime)
        return connectionTime
    }

    private fun finishTest()
    {
        val endTime = System.currentTimeMillis()
        val totalTime = if( startPathTimes.isEmpty() ) 0L else endTime - startPathTimes.first()

        println("Teste Finalizado")
        println("Tempo total: $totalTime milissegundos")
        println("Toques levantados: $touchUpCount")

        for (connection in connections)
        {
            println("${connection.circleOrigin.text} - ${connection.circleDestination.text}")
            println("  - Sequência correta: ${connection.isSequenceCorrect}")
            println("  - Caminho interrompido: ${connection.isPathInterrupted}")
            println("  - Tempo: ${connection.time} milissegundos")
        }

        //todo: isolar
        var i = 1
        var totalTimeTouchUp = 0L
        for( time in startTouchUpTimes)
        {
            val pauseInterval = startPathTimes[i] - time
            totalTimeTouchUp += pauseInterval
            println("Tempo toque levantado ${i}: $pauseInterval")
            i++
        }
        println("Tempo total toque levantado: $totalTimeTouchUp")

        isTestFinished = true
    }

}