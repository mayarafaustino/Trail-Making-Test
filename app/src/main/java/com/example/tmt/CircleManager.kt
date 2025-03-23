package com.example.tmt

class CircleManager {
    val circlesTmtA = mutableListOf(
        Circle(100f, 200f, "1", 0),
        Circle(300f, 500f, "2", 1),
        Circle(500f, 300f, "3", 2),
        Circle(700f, 600f, "4", 3),
        Circle(900f, 400f, "5", 4),
        Circle(800f, 800f, "6", 5)
    )

    val circlesTmtB = mutableListOf(
        Circle(100f, 200f, "1", 0),
        Circle(300f, 500f, "A", 1),
        Circle(500f, 300f, "2", 2),
        Circle(700f, 600f, "B", 3),
        Circle(900f, 400f, "3", 4),
        Circle(800f, 800f, "C", 5)
    )

    //somente as sequencias corretas
    val connectedCirclesIndexes = mutableListOf<Int>()

    fun isAllCirclesConnected(circlesTmtA: List<Circle>): Boolean {
        return connectedCirclesIndexes.size == circlesTmtA.size

    }
}