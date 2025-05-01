package com.example.tmt

import java.util.Locale
import java.util.Locale.setDefault

class TestResults(
    private val tmtType: TmtType,
    private val totalTime: Long,
    private val connections: List<Connection>,
    private val touchUpList: List<TouchUp>,
    private val circles: List<Circle> )
{

    companion object {
        val EMPTY = TestResults(TmtType.TMT_A_SAMPLE, 0, emptyList(), emptyList(), emptyList())
        init {
                setDefault(Locale("pt", "BR"))
            }

    }

    fun getTestType(): TmtType = tmtType

    private fun numExpectedConnections(): Int = circles.size - 1

    private fun Long.toSeconds(): String
    {
        return "%.3f".format(this / 1000.0) + "s"
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

    fun getTouchUpCount(): String
    {
        return "Toques levantados: ${touchUpList.size}"
    }

    fun getTotalTouchUpTime(): String {
        val total = touchUpList.sumOf { it.calculateDuration() }
        return "Tempo de toque levantado: ${total.toSeconds()}"
    }

    //temporario
    fun exibirResultados()
    {
        println(getTestType())
        println(getFormattedTotalTime())
        println(getTouchUpCount())
        println(getTotalTouchUpTime())
        println(getCorrectConnectionsCount())
        println(getIncorrectConnectionsCount())
        println(getCorrectConnectionsDetails())
        println(getIncorrectConnectionsDetails())
    }

}