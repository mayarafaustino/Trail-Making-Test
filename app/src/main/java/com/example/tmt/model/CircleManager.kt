package com.example.tmt.model

class CircleManager( private val screenWidth: Float,
                     private val screenHeight: Float,
                     private val tmtType: TmtType
)
{

    private val circles = CircleFactory.criarCirculos(tmtType, screenWidth, screenHeight)

    fun getCircles(): List<Circle> = circles

}