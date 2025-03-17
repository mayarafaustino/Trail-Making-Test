package com.example.tmt

class CircleManager {
    val circlesTmtA = mutableListOf(
        Circle(100f, 200f, "1"),
        Circle(300f, 500f, "2"),
        Circle(500f, 300f, "3"),
        Circle(700f, 600f, "4"),
        Circle(900f, 400f, "5"),
        Circle(800f, 800f, "6")
    )

    val circlesTmtB = mutableListOf(
        Circle(100f, 200f, "1"),
        Circle(300f, 500f, "A"),
        Circle(500f, 300f, "2"),
        Circle(700f, 600f, "B"),
        Circle(900f, 400f, "3"),
        Circle(800f, 800f, "C")
    )

    //somente as sequencias corretas
    val connectedCirclesIndexes = mutableListOf<Int>()

    fun isAllCirclesConnected(circlesTmtA: List<Circle>): Boolean {
        return connectedCirclesIndexes.size == circlesTmtA.size

    }
}