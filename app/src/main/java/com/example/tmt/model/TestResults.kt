package com.example.tmt.model

import java.util.Locale
import java.util.Locale.setDefault

class TestResults(
    private val tmtType: TmtType,
    private val totalTime: Long,
    private val connections: List<Connection>,
    private val liftList: List<Lift>,
    private val circles: List<Circle> )
{

    companion object {
        val EMPTY = TestResults(TmtType.TMT_A_SAMPLE, 0, emptyList(), emptyList(), emptyList())
        init
        {
            setDefault(Locale("pt", "BR"))
        }

    }

    fun getTestType(): TmtType = tmtType

    private fun numExpectedConnections(): Int = circles.size - 1

    private fun Long.toSeconds(): String
    {
        return "%.2f".format(this / 1000.0) + "s"
    }

    fun getFormattedTotalTime(): String
    {
        return "Tempo total: ${totalTime.toSeconds()}"
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

    fun getDetails(): String
    {
        return buildString {
            append(getCorrectConnectionsDetails() + "\n" + getIncorrectConnectionsDetails())
        }
    }

    private fun getCorrectConnectionsDetails(): String
    {
        val correctConnections = connections.filter { it.isSequenceCorrect && !it.isRepeated }

        if (correctConnections.isEmpty()) return "Nenhuma conexão correta registrada."

        return buildString {
            append("Conexões corretas:\n")
            correctConnections.forEachIndexed { index, conn ->
                append("${index + 1}) ${conn.from.text} ➝ ${conn.to.text} - Tempo: ${conn.time.toSeconds()}\n")
            }
        }
    }

    private fun getIncorrectConnectionsDetails(): String
    {
        val incorrectConnections = connections.filter { !it.isSequenceCorrect || it.isRepeated }

        if (incorrectConnections.isEmpty()) return "Nenhuma conexão incorreta registrada."

        return buildString {
            append("Conexões incorretas:\n")
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

    fun getLiftsCount(): String
    {
        return "Número de interrupções no traçado: ${liftList.size}"
    }

    fun getLiftsTotalTime(): String
    {
        val total = liftList.sumOf { it.calculateDuration() }
        return "Tempo total em interrupções: ${total.toSeconds()}"
    }

    fun getTotalTimeWithoutLifts(): String
    {
        val totalTimeWithoutLifts = totalTime - liftList.sumOf { it.calculateDuration() }
        return "Tempo líquido (desconsiderando interrupções): ${totalTimeWithoutLifts.toSeconds()}"
    }
}