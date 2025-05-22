package com.example.tmt.model

import kotlin.math.sqrt

class TestManager(
    private val tmtType: TmtType,
    screen : ScreenSize
)
{
    private val circles = CircleManager(screen.screenWidth, screen.screenHeight, tmtType).getCircles()
    private val circleTimes = mutableListOf<Long>()
    private val connections = mutableListOf<Connection>()
    private val circlesTouchedIndex = mutableListOf<Int>()
    private var lastCircleTouchedIndex = -1
    private var leftTheLastCircle = false
    private var isTestFinished = false
    private var isMaxTimeExceeded = false

    fun getCircles(): List<Circle> = circles

    fun isTestFinished(): Boolean = isTestFinished

    fun checkConnection(pathManager: PathManager)
    {
        if( isMaxTimeExceeded( pathManager.pathStartTimes.first()) )
        {
            isMaxTimeExceeded = true
            return
        }

        val currentCircleIndex = verifyCurrentCircleIndex(pathManager)
        if( currentCircleIndex == -1 )
            return

        val isFirstCircle = lastCircleTouchedIndex == -1
        val nextCorrectCircleIndex = if( isFirstCircle ) 0 else lastCircleTouchedIndex + 1
        val isSequenceCorrect = currentCircleIndex == nextCorrectCircleIndex

        if( isFirstCircle )
            recordCircleTouch(currentCircleIndex)

        if( !isFirstCircle && leftTheLastCircle )
            doConnection(currentCircleIndex, isSequenceCorrect, pathManager)

        lastCircleTouchedIndex = currentCircleIndex
        leftTheLastCircle = false
    }

    private fun verifyCurrentCircleIndex(pathManager: PathManager): Int
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

    private fun pointIsWithinCircle( point: Pair<Float, Float>, circle: Circle): Boolean
    {
        val distance = calculateDistance(point, circle)
        return distance <= circle.radius
    }

    private fun calculateDistance( point: Pair<Float, Float>, circle: Circle): Float
    {
        return sqrt((point.first - circle.x) * (point.first - circle.x) +
                (point.second - circle.y) * (point.second - circle.y))
    }

    private fun doConnection(currentCircleIndex: Int, isSequenceCorrect: Boolean, pathManager: PathManager)
    {
        recordCircleTouch(currentCircleIndex)

        val lastCircle = circles[lastCircleTouchedIndex]
        val currentCircle = circles[currentCircleIndex]

        val isPathInterrupted = !checkPathBetweenCircles( lastCircle, currentCircle, pathManager )

        val isRepeated = connections.any {
            (it.from == lastCircle && it.to == currentCircle)
        }

        val connection = Connection(lastCircle,
            currentCircle,
            isPathInterrupted,
            timeBetweenLastConnection(),
            isSequenceCorrect,
            isRepeated)
        connections.add(connection)

    }

    //retorna se algum path toca nos dois círculos
    private fun checkPathBetweenCircles(circle1: Circle, circle2: Circle, pathManager: PathManager): Boolean
    {
        val circle1Touched = pathManager.pathPoints.any { pointIsWithinCircle(it, circle1) }
        val circle2Touched = pathManager.pathPoints.any { pointIsWithinCircle(it, circle2) }

        return circle1Touched && circle2Touched
    }

    //retorna o tempo em segundos entre o ultimo e penultimo circulo conectado
    private fun timeBetweenLastConnection(): Long
    {
        val timeLastCircle = circleTimes.last()
        val timePenultCircle = circleTimes[circleTimes.size - 2]
        return timeLastCircle - timePenultCircle
    }

    fun recordCircleTouch(circleIndex: Int)
    {
        circlesTouchedIndex.add(circleIndex)
        circleTimes.add(System.currentTimeMillis())
    }

    fun checkEndTest(): Boolean
    {
        if( isMaxTimeExceeded )
        {
            isTestFinished = true
            return isTestFinished
        }

        if( connections.isEmpty())
            return false

        val isLastCircleTouched = connections.last().to.ordem == circles.lastIndex

        if ( isLastCircleTouched && isAllCirclesTouched())
            isTestFinished = true

        return isTestFinished
    }

    private fun isAllCirclesTouched(): Boolean
    {
        return circles.indices.all { it in circlesTouchedIndex }
    }

    fun createResults(pathManager: PathManager): TestResults
    {
        if (tmtType.isSample())
            return TestResults.EMPTY

        val maxDurationMillis = tmtType.maxDurationSeconds * 1000L
        val totalTime = if (isMaxTimeExceeded) maxDurationMillis else calculateTotalTime(pathManager)
        return TestResults(tmtType, totalTime, connections, pathManager.liftList, circles)
    }

    private fun calculateTotalTime(pathManager: PathManager): Long
    {
        val endTime = System.currentTimeMillis()
        val totalTime = if (pathManager.pathStartTimes.isEmpty()) 0L else endTime - pathManager.pathStartTimes.first()
        return totalTime
    }

    private fun isMaxTimeExceeded(startTimeMillis: Long): Boolean
    {
        if(tmtType.isSample())
            return false

        val elapsed = System.currentTimeMillis() - startTimeMillis
        val maxDurationMillis = tmtType.maxDurationSeconds * 1000
        return elapsed >= maxDurationMillis
    }
}