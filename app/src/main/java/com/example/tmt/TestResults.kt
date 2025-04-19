package com.example.tmt

class TestResults(
    private val tmtType: TmtType,
    private val totalTime: Long,
    private val connections: List<Connection>,
    private val touchUpList: List<TouchUp>,
    private val circles: List<Circle> )
{
    fun getTestType(): String
    {
        return when (tmtType)
        {
            TmtType.TMT_A -> "TMT-A"
            TmtType.TMT_B -> "TMT-B"
            TmtType.TMT_A_SAMPLE -> "TMT-A Sample"
            TmtType.TMT_B_SAMPLE -> "TMT-B Sample"
        }
    }

    private fun numExpectedConnections(): Int = circles.size - 1

    private fun Long.toSeconds(): String
    {
        return "%.3f".format(this / 1000.0) + "s"
    }

    fun getFormattedTotalTime(): String
    {
        return "Tempo total de teste: ${totalTime.toSeconds()}"
    }

    fun getCorrectConnectionsCount(): String
    {
        val count = connections.count { it.isSequenceCorrect && !it.isRepeated }
        return "Conexões corretas: $count / ${numExpectedConnections()}"
    }

    fun getIncorrectConnectionsCount(): String
    {
        val count = connections.count { !it.isSequenceCorrect || it.isRepeated }
        return "Conexões incorretas: $count"
    }

    fun getCorrectConnectionsDetails(): String
    {
        val correctConnections = connections.filter { it.isSequenceCorrect && !it.isRepeated }

        if (correctConnections.isEmpty()) return "Nenhuma conexão correta registrada."

        return buildString {
            append("Detalhes das conexões corretas:\n")
            correctConnections.forEachIndexed { index, conn ->
                append("${index + 1}) ${conn.from.text} ➝ ${conn.to.text} - Tempo: ${conn.time.toSeconds()}\n")
            }
        }
    }

    fun getIncorrectConnectionsDetails(): String
    {
        val incorrectConnections = connections.filter { !it.isSequenceCorrect || it.isRepeated }

        if (incorrectConnections.isEmpty()) return "Nenhuma conexão incorreta registrada."

        return buildString {
            append("Detalhes das conexões incorretas:\n")
            incorrectConnections.forEachIndexed { index, conn ->
                val motivo = when {
                    !conn.isSequenceCorrect -> "sequência incorreta"
                    conn.isRepeated -> "conexão repetida"
                    else -> "motivo desconhecido"
                }

                append("${index + 1}) ${conn.from.text} ➝ ${conn.to.text} - Tempo: ${conn.time.toSeconds()} (${motivo})\n")
            }
        }
    }

    fun getTouchUpCount(): String
    {
        return "Total de toques levantados: ${touchUpList.size}"
    }

    fun getTotalTouchUpTime(): String {
        val total = touchUpList.sumOf { it.calculateDuration() }
        return "Tempo total com o dedo levantado: ${total.toSeconds()}"
    }


}