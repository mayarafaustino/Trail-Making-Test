package com.example.tmt
import android.content.Context
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Locale.setDefault


class PdfGenerator {

    companion object {
        init {
            setDefault(Locale("pt", "BR"))
        }
    }

    fun generatePdf(context: Context, htmlContent: String,patientName: String? = null, patientIdentifier: String? = null, onFinish: (file: File?) -> Unit) {
        val webView = WebView(context)
        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "capture.pdf")
                val printAdapter = webView.createPrintDocumentAdapter("$patientName - $patientIdentifier")
                context.getSystemService(android.print.PrintManager::class.java)
                    ?.print("$patientName - $patientIdentifier", printAdapter, null)
                onFinish(file)
            }
        }
    }

    //fun createHtmlPDF(patientName: String? = null, patientAge: Int? = null, patientIdentifier: String? = null, professionalName: String? = null, professionalIdentifier: String? = null, imageTMTABase64: String? = null, imageTMTBBase64: String? = null ): String {

    fun createHtmlPDF(): String {

        val calendar = Calendar.getInstance()
        val changesFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = changesFormat.format(calendar.time)
        //correctConnections["TestA"]
        val timeInformations = mutableMapOf(
            "Total" to 0,
            "Active" to 0,
            "Stopped" to 0,
            "TotalA" to 0,
            "ActiveA" to 0,
            "StoppedA" to 0,
            "TotalB" to 0,
            "ActiveB" to 0,
            "StoppedB" to 0,
        )
        val countTouchUp = mutableMapOf(
            "Total" to 0,
            "TestA" to 0,
            "TestB" to 0
        )
        val correctConnections = mutableMapOf(
            "Total" to 0,
            "TestA" to 0,
            "TestB" to 0
        )
        val wrongConnections = mutableMapOf(
            "Total" to 0,
            "TestA" to 0,
            "TestB" to 0
        )

        val testDetails = mutableMapOf(
            "TestA" to "",
            "TestB" to ""
        )

        var expectedValueA = 0
        var expectedValueB = 0



        //correctConnections["TestA"]

        TestSession.results.forEach { result ->
            if (result.getTestType() == TmtType.TMT_A)
            {
                // Preencher dados do TMT-A
                timeInformations["Total"] = timeInformations["Total"]!! + result.getFormattedTotalTime().filter { it.isDigit() }.toInt()
                timeInformations["TotalA"] = result.getFormattedTotalTime().filter { it.isDigit() }.toInt()

                //textTempoTotalA.text          = result.getFormattedTotalTime()
                //textTempoToqueLevantadoA.text = result.getTotalTouchUpTime()
                timeInformations["Stopped"] = timeInformations["Stopped"]!! + result.getTotalTouchUpTime().filter { it.isDigit() }.toInt()
                timeInformations["StoppedA"] = result.getTotalTouchUpTime().filter { it.isDigit() }.toInt()

                //tempo ativo
                timeInformations["Active"] = timeInformations["Active"]!! + (timeInformations["TotalA"]!! - timeInformations["StoppedA"]!!)
                timeInformations["ActiveA"] = timeInformations["TotalA"]!! - timeInformations["StoppedA"]!!


                //textToquesLevantadosA.text    = result.getTouchUpCount()

                countTouchUp["Total"] = countTouchUp["Total"]!! + result.getTouchUpCount().filter { it.isDigit() }.toInt()
                countTouchUp["TestA"] = result.getTouchUpCount().filter { it.isDigit() }.toInt()


                //textConexoesCorretasA.text    = result.getCorrectConnectionsCount().filter { it.isDigit() }.toInt()
                // Remove o texto fixo e pega só a parte numérica
                val numbersPart = result.getCorrectConnectionsCount().substringAfter(":").trim() // "5/ 25"
                // Divide pelos separador "/"
                val parts = numbersPart
                    .split("/")
                    .map { part ->
                        val digitsOnly = part.filter { it.isDigit() }
                        digitsOnly.toIntOrNull() ?: 0
                    }
                // Atribui aos valores
                val current = parts.getOrNull(0) ?: 0

                if(current != 0)
                {
                    correctConnections["Total"] = correctConnections["Total"]!! + current
                    correctConnections["TestA"] = current
                    expectedValueA = parts.getOrNull(1) ?: 0
                }



                //textConexoesIncorretasA.text  = result.getIncorrectConnectionsCount().filter { it.isDigit() }.toInt()
                wrongConnections["Total"] = wrongConnections["Total"]!! +
                        (result.getIncorrectConnectionsCount().filter { it.isDigit() }?.toIntOrNull() ?: 0)
                wrongConnections["TestA"] = (result.getIncorrectConnectionsCount().filter { it.isDigit() }?.toIntOrNull() ?: 0)

                //textDetalheTmtA.text          = result.getDetails()
                testDetails["TestA" ] = result.getDetails()
            }
            else if (result.getTestType() == TmtType.TMT_B)
            {
                // Preencher dados do TMT-B
                //textTempoTotalB.text          = result.getFormattedTotalTime()
                timeInformations["Total"] = timeInformations["Total"]!! + result.getFormattedTotalTime().filter { it.isDigit() }.toInt()
                timeInformations["TotalB"] = result.getFormattedTotalTime().filter { it.isDigit() }.toInt()

                //textTempoTotalA.text          = result.getFormattedTotalTime()
                //textTempoToqueLevantadoA.text = result.getTotalTouchUpTime()
                timeInformations["Stopped"] = timeInformations["Stopped"]!! + result.getTotalTouchUpTime().filter { it.isDigit() }.toInt()
                timeInformations["StoppedB"] = result.getTotalTouchUpTime().filter { it.isDigit() }.toInt()

                //tempo ativo
                timeInformations["Active"] = timeInformations["Active"]!! + (timeInformations["TotalB"]!! - timeInformations["StoppedB"]!!)
                timeInformations["ActiveB"] = timeInformations["TotalB"]!! - timeInformations["StoppedB"]!!

                //textToquesLevantadosB.text    = result.getTouchUpCount()
                countTouchUp["Total"] = countTouchUp["Total"]!! + result.getTouchUpCount().filter { it.isDigit() }.toInt()
                countTouchUp["TestB"] = result.getTouchUpCount().filter { it.isDigit() }.toInt()

                //textConexoesCorretasB.text    = result.getCorrectConnectionsCount()
                // Remove o texto fixo e pega só a parte numérica
                val numbersPart = result.getCorrectConnectionsCount().substringAfter(":").trim() // "5/ 25"
                // Divide pelos separador "/"
                val parts = numbersPart
                    .split("/")
                    .map { part ->
                        val digitsOnly = part.filter { it.isDigit() }
                        digitsOnly.toIntOrNull() ?: 0
                    }
                // Atribui aos valores
                val current = parts.getOrNull(0) ?: 0

                if(current != 0){
                    correctConnections["Total"] = correctConnections["Total"]!! + current
                    correctConnections["TestB"] = current
                    expectedValueB = parts.getOrNull(1) ?: 0
                }

                //textConexoesIncorretasB.text  = result.getIncorrectConnectionsCount()
                wrongConnections["Total"] = wrongConnections["Total"]!! +
                        (result.getIncorrectConnectionsCount().filter { it.isDigit() }?.toIntOrNull() ?: 0)
                wrongConnections["TestB"] = (result.getIncorrectConnectionsCount().filter { it.isDigit() }?.toIntOrNull() ?: 0)


                //textDetalheTmtB.text          = result.getDetails()
                testDetails["TestB" ] = result.getDetails()
            }


        }

        val timeDif = timeInformations["ActiveB"]!! - timeInformations["ActiveA"]!!
        val ratio = (timeInformations["ActiveB"]!! / timeInformations["ActiveA"]!!).toDouble()
        val interferenceScore = (timeInformations["ActiveB"]!!-timeInformations["ActiveA"]!!)/timeInformations["ActiveA"]!!.toDouble()



        /*val paragrafosHtml = itens.joinToString("\n") { item ->
            """<p>$item</p>"""


            <p style="font-weight: 700;">Tempo total : 300s</p>
                        <p >- Tempo líquido: 200s </p>
                        <p >- Tempo em pausa: 100s</p>
                        <br><br>
                        <p style="font-weight: 700;">Números:</p>
                        <p >- Conexões corretas: </p>
                        <p >- Conexões erradas: </p>
        }*/




        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    @page { size: A4; margin:0; margin-top:20mm; margin-bottom: 20mm; }
                    html, body { width: 100%; height: 100%; margin: 0; padding: 0; font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif ;}
                    .capa,.resultadogeral,.tmtimagem,.tmtdados{ width: 100vw; height: 100vh; display: flex; justify-content: center; align-items: center; }
                    .full-image { max-width: 100%; max-height: 100vh; object-fit: contain; }
                    .capa{flex-direction: column; width: 210mm; /* Largura A4 */height: 297mm;}
                    h1{font-size: 160px; text-align: center; margin-top: 0;}
                    .textonormal{margin-bottom: 0; align-self:start; margin-left: 10%; font-size: 20px;}
                    .resultadogeral,.tmtdados{justify-content: start; width: 210mm; ;flex-direction: column;page-break-after: always; break-after: page; margin-bottom: 20mm; min-height: 297mm}
                    h2{font-size: 70px; margin-top: 8%;margin-bottom: 4%;}
                    hr{border-radius: 2px;; background-color:#05A483 ; width: 166mm; margin:0px; margin-bottom: 4%; height: 8px}
                    h3{margin: 0; font-size: 30px;}
                    p{font-size: 20px; margin: 0;}
                   
                    
                </style>
            </head>
            <body>
                <div class="capa">
                    <h1>Trail<br/>Making<br/>Test<br/></h1>
                    <p class="textonormal" id="nomedopaciente">Paciente: ${TestSession.patientName} - ${TestSession.patientAge} anos</p>
                    <p class="textonormal" id="identificador">Identificador: ${TestSession.patientIdentifier}</p>
                    <p class="textonormal" id="nomedoprofissional">Profissional: ${TestSession.professionalName} - ${TestSession.professionalIdentifier}</p>
                    <p class="textonormal" id="data"> Data: $date </p>
                </div>
                <div class="resultadogeral">
                    <h2 class="titulo">Resultado geral</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;">Tempo total : ${"%.3f".format(timeInformations["Total"]!!.toDouble() / 1000)}s</p>
                        <p >- Tempo líquido(total): ${"%.3f".format(timeInformations["Active"]!!.toDouble() / 1000)}s </p>
                        <p >- Tempo em pausa(total): ${"%.3f".format(timeInformations["Stopped"]!!.toDouble() / 1000)}s</p>
                        <br><br>
                        <h3>Métricas</h3>
                        <hr>
                        <p style="font-weight: 700;"> Conexões e Interrupções:</p>
                        <p >- Conexões corretas:${
                            if(correctConnections["Total"] != 0)
                            {
                                correctConnections["Total"]} else {"Nenhuma conexão correta registrada."}}</p>
                        <p >- Conexões erradas: ${
                            if(wrongConnections["Total"] != 0){
                                wrongConnections["Total"]} else {"Nenhuma conexão incorreta registrada."}}</p>
                                                  
                        <p >- Interrupções(Ausência de toque): ${countTouchUp["Total"]}</p>
                        <br><br>
                        <p style="font-weight: 700;"> Outras informações:</p>
                        <p>- Diferença (TMT-B - TMT-A):${"%.3f".format(timeDif / 1000) }</p>
                        <p>- Razão (TMT-B / TMT-A):${"%.3f".format(ratio / 1000)}</p>
                        <p>- Pontuação de Interferência( (TMT-B - TMT-A) / TMT-A): ${"%.3f".format(interferenceScore/ 1000)} </p>
                        
                        
                    </div>
                </div>
                <div class="tmtimagem">
                    <img src="data:image/png;base64,${TestSession.imageTMTA}" class="full-image" />
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-A</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;" >Tempo total : ${"%.3f".format(timeInformations["TotalA"]!!.toDouble() / 1000)}s</p>
                        <p >- Tempo líquido: ${"%.3f".format(timeInformations["ActiveA"]!!.toDouble() / 1000)}s </p>
                        <p >- Tempo em pausa: ${"%.3f".format(timeInformations["StoppedA"]!!.toDouble() / 1000)}s</p>
                       
                        <br><br>
                        <h3>Métricas</h3>
                        <hr>
                        <p style="font-weight: 700;"> Conexões e Interrupções:</p>
                        <p>- Conexões corretas: ${

                            if(correctConnections["TestA"] != 0)
                            {
                                "${correctConnections["TestA"]} / $expectedValueA "} else {"Nenhuma conexão correta registrada."}}
                            
                        </p>
                           
                        <p>- Conexões erradas: ${
                            if(wrongConnections["TestA"] != 0)
                            {
                                wrongConnections["TestA"]} else {"Nenhuma conexão incorreta registrada."}}
                            
                        </p>
                        <p >- Interrupções(Ausência de toque): ${countTouchUp["TestA"]}</p>
                        

                        <br><br>
                        <h3> Detalhes</h3>
                        <hr>
                        <p> ${testDetails["TestA"]?.replace("\n", "<br>")}</p>


                    </div>
                </div>
                <div class="tmtimagem">
                    <img src="data:image/png;base64,${TestSession.imageTMTB}" class="full-image" />
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-B</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;" >Tempo total : ${"%.3f".format(timeInformations["TotalB"]!!.toDouble() / 1000)}s</p>
                        <p >- Tempo líquido: ${"%.3f".format(timeInformations["ActiveB"]!!.toDouble() / 1000)}s </p>
                        <p >- Tempo em pausa: ${"%.3f".format(timeInformations["StoppedB"]!!.toDouble() / 1000)}s</p>
                        
                         
                        <br><br>
                        <h3> Métricas</h3>
                        <hr>
                        <p >- Conexões corretas:${if(correctConnections["TestB"]!=0)
                        {
                                "${correctConnections["TestB"]} / $expectedValueB "} else {"Nenhuma conexão correta registrada."}}

                        <p >- Conexões erradas: ${
                            if (wrongConnections["TestB"] != 0) {
                                wrongConnections["TestB"]} else {"Nenhuma conexão incorreta registrada."}}</p>
                        <p >- Interrupções(Ausência de toque): ${countTouchUp["TestB"]}</p>
                         <br><br>
                        
                        <h3> Detalhes</h3>
                        <hr>
                        <p> ${testDetails["TestB"]?.replace("\n", "<br>")}</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
