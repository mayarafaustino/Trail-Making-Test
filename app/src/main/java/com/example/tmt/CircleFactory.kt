package com.example.tmt

object CircleFactory
{
    fun criarCirculos(tipo: TmtType, screenWidth: Float, screenHeight: Float): List<Circle>
    {
        return when (tipo) {
            TmtType.TMT_A -> criarTmtA(screenWidth, screenHeight)
            TmtType.TMT_B -> criarTmtB(screenWidth, screenHeight)
            TmtType.TMT_A_SAMPLE -> criarTmtASample(screenWidth, screenHeight)
            TmtType.TMT_B_SAMPLE -> criarTmtBSample(screenWidth, screenHeight)
        }
    }

    private fun criarTmtA(w: Float, h: Float): List<Circle>
    {
        return listOf(
            Circle(0.60f * w, 0.51f * h, "1", 0),
            Circle(0.52f * w, 0.59f * h, "2", 1),
            Circle(0.76f * w, 0.65f * h, "3", 2),
            Circle(0.67f * w, 0.33f * h, "4", 3),
            Circle(0.41f * w, 0.35f * h, "5", 4),
            Circle(0.53f * w, 0.42f * h, "6", 5),
            Circle(0.40f * w, 0.50f * h, "7", 6),
            Circle(0.26f * w, 0.63f * h, "8", 7),
            Circle(0.26f * w, 0.80f * h, "9", 8),
            Circle(0.56f * w, 0.71f * h, "10", 9),
            Circle(0.42f * w, 0.76f * h, "11", 10),
            Circle(0.13f * w, 0.76f * h, "12", 11),
            Circle(0.15f * w, 0.55f * h, "13", 12),
            Circle(0.27f * w, 0.51f * h, "14", 13),
            Circle(0.17f * w, 0.33f * h, "15", 14),
            Circle(0.30f * w, 0.38f * h, "16", 15),
            Circle(0.45f * w, 0.21f * h, "17", 16),
            Circle(0.15f * w, 0.10f * h, "18", 17),
            Circle(0.25f * w, 0.22f * h, "19", 18),
            Circle(0.49f * w, 0.12f * h, "20", 19),
            Circle(0.70f * w, 0.24f * h, "21", 20),
            Circle(0.78f * w, 0.10f * h, "22", 21),
            Circle(0.85f * w, 0.45f * h, "23", 22),
            Circle(0.78f * w, 0.84f * h, "24", 23),
            Circle(0.44f * w, 0.89f * h, "25", 24)
        )
    }

    private fun criarTmtB(w: Float, h: Float): List<Circle>
    {
        return listOf(
            Circle(0.52f * w, 0.39f * h, "1", 0),
            Circle(0.71f * w, 0.60f * h, "A", 1),
            Circle(0.32f * w, 0.72f * h, "2", 2),
            Circle(0.44f * w, 0.23f * h, "B", 3),
            Circle(0.44f * w, 0.33f * h, "3", 4),
            Circle(0.86f * w, 0.48f * h, "C", 5),
            Circle(0.57f * w, 0.20f * h, "4", 6),
            Circle(0.79f * w, 0.17f * h, "D", 7),
            Circle(0.83f * w, 0.42f * h, "5", 8),
            Circle(0.83f * w, 0.80f * h, "E", 9),
            Circle(0.45f * w, 0.75f * h, "6", 10),
            Circle(0.22f * w, 0.82f * h, "F", 11),
            Circle(0.30f * w, 0.36f * h, "7", 12),
            Circle(0.21f * w, 0.58f * h, "G", 13),
            Circle(0.20f * w, 0.17f * h, "8", 14),
            Circle(0.22f * w, 0.43f * h, "H", 15),
            Circle(0.34f * w, 0.15f * h, "9", 16),
            Circle(0.67f * w, 0.16f * h, "I", 17),
            Circle(0.87f * w, 0.10f * h, "10", 18),
            Circle(0.87f * w, 0.62f * h, "J", 19),
            Circle(0.88f * w, 0.88f * h, "11", 20),
            Circle(0.14f * w, 0.88f * h, "K", 21),
            Circle(0.15f * w, 0.50f * h, "12", 22),
            Circle(0.16f * w, 0.76f * h, "L", 23),
            Circle(0.14f * w, 0.11f * h, "13", 24)
        )
    }

    private fun criarTmtASample(w: Float, h: Float): List<Circle>
    {
        return listOf(
            Circle(0.54f * w, 0.45f * h, "1", 0),
            Circle(0.59f * w, 0.31f * h, "2", 1),
            Circle(0.76f * w, 0.40f * h, "3", 2),
            Circle(0.66f * w, 0.44f * h, "4", 3),
            Circle(0.71f * w, 0.56f * h, "5", 4),
            Circle(0.29f * w, 0.53f * h, "6", 5),
            Circle(0.21f * w, 0.38f * h, "7", 6),
            Circle(0.42f * w, 0.37f * h, "8", 7)
        )
    }

    private fun criarTmtBSample(w: Float, h: Float): List<Circle>
    {
        return listOf(
            Circle(0.53f * w, 0.46f * h, "1", 0),
            Circle(0.59f * w, 0.31f * h, "A", 1),
            Circle(0.76f * w, 0.40f * h, "2", 2),
            Circle(0.66f * w, 0.44f * h, "B", 3),
            Circle(0.71f * w, 0.56f * h, "3", 4),
            Circle(0.29f * w, 0.52f * h, "C", 5),
            Circle(0.20f * w, 0.38f * h, "4", 6),
            Circle(0.41f * w, 0.36f * h, "D", 7)
        )
    }

}