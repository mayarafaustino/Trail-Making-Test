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


    private var currentCircle = 0

    private var lastConnectedCircle = -1
    private var touchUpCount = 0
    private val outOfOrderCircles = mutableSetOf<Int>()
    private var outOfOrderCount = 0
//    private val outOfOrderAttempts = mutableMapOf<Int, Int>() //Mapa para tentativas fora de ordem
//    private val outOfOrderAttempts = Pair
    private val outOfOrderAttempts = mutableListOf<OutOfOrderAttempt>()
    private var lastCircleTouched = -1

    private var startTime = 0L //Tempo inicial
    private val pointTimes = mutableListOf<Long>() //Tempo entre pontos
    private val circleTimes = mutableListOf<Long>() //Tempo entre círculos
    private val connectedCirclesSet = mutableSetOf<Int>() //Conjunto de círculos conectados
    private val outOfOrderSet = mutableSetOf<Int>() //Conjunto de circulos fora de ordem ja contados.

    override fun onDraw(canvas: Canvas) {
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
                if (!pathManager.isDrawing) {
                    pathManager.startPath(x, y)
                    startTime = System.currentTimeMillis() //Inicia o timer
                    pointTimes.clear() //Limpa tempos entre pontos
                    pointTimes.add(startTime) //Adiciona tempo inicial
                } else {
                    pathManager.updatePath(x, y)
                    pointTimes.add(System.currentTimeMillis()) //Adiciona tempo
                }
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (pathManager.isDrawing) {
                    pathManager.updatePath(x, y)
                    checkConnection()
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                pathManager.finishPath()
                touchUpCount++
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

    //TODO verificar se passou por todos os círculos && chegou no último círculo
    private fun checkEndTest()
    {
        if (circleManager.isAllCirclesConnected(circlesA))
            finishGame()
    }

    private fun lastCircleIsValid(nextCircleIndex: Int): Boolean
    {
        val lastCircleConnectedIsPreviousCircle = circleManager.connectedCirclesIndexes.last() == nextCircleIndex - 1
        val pathBetweenCircles = checkPathBetweenCircles( circlesA[circleManager.connectedCirclesIndexes.last()], circlesA[nextCircleIndex] )

        return lastCircleConnectedIsPreviousCircle && pathBetweenCircles
    }

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

    private var leftTheLastCircle = false

    private fun verifyCurrentCircleIndex(circles: MutableList<Circle>): Int
    {
        var currentCircleIndex = -1

        for(i in 0 until circles.size)
        {
            if( !pointIsWithinCircle( pathManager.pathPoints.last(), circles[i] ) )
            {
                if( i == lastCircleTouched )
                    leftTheLastCircle = true
                continue
            }
            currentCircleIndex = i
        }
        return currentCircleIndex
    }

    private fun checkConnection()
    {
        if (circleManager.isAllCirclesConnected(circlesA))
            return

        var currentCircleIndex = verifyCurrentCircleIndex(circlesA)
        if( currentCircleIndex == -1 )
            return

        val nextCorrectCircleIndex = circleManager.connectedCirclesIndexes.size
        val isOutOfOrderCircle = currentCircleIndex != nextCorrectCircleIndex

        //Quando o círculo está na ordem correta
        if( !isOutOfOrderCircle )
        {
            val noCircleIsConnected = circleManager.connectedCirclesIndexes.isEmpty()

            if( noCircleIsConnected || lastCircleIsValid(currentCircleIndex) )
            {
                if( !noCircleIsConnected )
                    Log.d( "Connection", "Círculo ${circlesA[currentCircleIndex - 1].text} conectado ao círculo ${circlesA[currentCircleIndex].text}" )

                circleManager.connectedCirclesIndexes.add(currentCircleIndex)
                circleTimes.add(System.currentTimeMillis()) //Adiciona tempo entre círculos
                checkEndTest()
            }
        }

        // Quando o círculo está fora de ordem
        //segunda condição previne que conte mais de uma vez enquanto o traço ainda está passando pelo círculo
        if ( isOutOfOrderCircle && ( leftTheLastCircle || lastCircleTouched == -1 ) )
        {
            val circleIsAlreadyConnected = circleManager.connectedCirclesIndexes.contains(currentCircleIndex)

            if( !outOfOrderSet.contains(currentCircleIndex) )
                outOfOrderSet.add(currentCircleIndex)

            outOfOrderAttempts.add(OutOfOrderAttempt(currentCircleIndex, circleIsAlreadyConnected))
            Log.d("OutOfOrder", "Passou pelo círculo ${circlesA[currentCircleIndex].text} fora de ordem!")
        }
        lastCircleTouched = currentCircleIndex
        leftTheLastCircle = false
    }

    private fun finishGame()
    {
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime)
        var finishTime = 0L


        for (i in 1 until circleTimes.size) {
            val timeDiff = circleTimes[i] - circleTimes[i - 1]
            val secondsDiff = TimeUnit.MILLISECONDS.toSeconds(timeDiff)
            finishTime += secondsDiff
        }

        println("Parabéns! Você conectou todos os círculos em ${finishTime} segundos!")

        println("Toques levantados: $touchUpCount")

        if (outOfOrderAttempts.isNotEmpty())
        {
            println("Tentativas fora de ordem: ${outOfOrderAttempts.size}")
            detailsOutOfOrder()
        }

        for (i in 1 until circleTimes.size) {
            val timeDiff = circleTimes[i] - circleTimes[i - 1]
            val secondsDiff = TimeUnit.MILLISECONDS.toSeconds(timeDiff)
            println("Tempo entre o círculo ${i} e ${i + 1}: ${secondsDiff} segundos")
        }
    }

    private fun detailsOutOfOrder( )
    {
        // Agrupa por círculo e conta as repetições e avanços
        val groupedCounts = outOfOrderAttempts.groupBy { it.circleIndex }
            .mapValues { (_, list) ->
                val repetitions = list.count { it.isRepetition }
                val advances = list.size - repetitions
                Pair(repetitions, advances)
            }

        // Exibe os resultados
        for ((circleIndex, counts) in groupedCounts) {
            println("Círculo ${circlesA[circleIndex].text}:")
            println("  - Repetições: ${counts.first}")
            println("  - Avanços: ${counts.second}")
        }
    }
}