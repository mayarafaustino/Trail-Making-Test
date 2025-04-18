package com.example.tmt

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs)
{
    private val pathManager = PathManager()
    private lateinit var testResults: TestResults
    private lateinit var testManager: TestManager
    private lateinit var circles: List<Circle>

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)

        val screen = ScreenSize(w.toFloat(), h.toFloat())
        testManager = TestManager( TmtType.TMT_B_SAMPLE, screen )
        circles = testManager.getCircles()
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        //Desenha os círculos
        circles.forEachIndexed { index, circle ->
            canvas.drawCircle(circle.x, circle.y, circle.radius, circle.paint)
            canvas.drawCircle(circle.x, circle.y, circle.radius, circle.strokePaint)
            canvas.drawText(circle.text, circle.x, circle.y + 15, circle.textPaint)

            if(index == 0)
                canvas.drawText("Início", circle.x, circle.y + circle.radius + circle.textPaint.textSize + 15, circle.textPaint)

            if(index == circles.lastIndex)
                canvas.drawText("Fim", circle.x, circle.y + circle.radius + circle.textPaint.textSize + 15, circle.textPaint)
        }

        //Desenha os caminhos
        pathManager.paths.forEach { path -> canvas.drawPath(path, pathManager.pathPaint) }

        if (pathManager.isDrawing)
            canvas.drawPath(pathManager.currentPath, pathManager.pathPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val x = event.x
        val y = event.y

        when (event.action)
        {
            MotionEvent.ACTION_DOWN -> {
                if (!pathManager.isDrawing)
                    pathManager.startPath(x, y)
                else
                    pathManager.updatePath(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (pathManager.isDrawing && !testManager.isTestFinished())
                {
                    pathManager.updatePath(x, y)
                    testManager.checkConnection( pathManager )
                    if( testManager.checkEndTest() )
                    {
                        testResults = testManager.createResults(pathManager)
                        exibirResultados()
                    }
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                pathManager.finishPath()
            }
        }
        return true
    }

    //temporario
    fun exibirResultados()
    {
        println(testResults.getTestType())
        println(testResults.getFormattedTotalTime())
        println(testResults.getTouchUpCount())
        println(testResults.getTotalTouchUpTime())
        println(testResults.getCorrectConnectionsCount())
        println(testResults.getIncorrectConnectionsCount())
        println(testResults.getCorrectConnectionsDetails())
        println(testResults.getIncorrectConnectionsDetails())
    }

}