package com.example.tmt.model
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

    fun createHtmlPDF(): String
    {
        val resultA = TestSession.results.find { it.getTestType() == TmtType.TMT_A }
        val resultB = TestSession.results.find { it.getTestType() == TmtType.TMT_B }

        if (resultA == null || resultB == null)
            return "TestResults for TMT-A and TMT-B not found"

        val calendar = Calendar.getInstance()
        val changesFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = changesFormat.format(calendar.time)

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>       
                    @page { size: A4; margin:0; margin-top:20mm; margin-bottom: 20mm; }
                    html, body { width: 100%; height: 100%; margin: 0; padding: 0; font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif ;}
                    .capa,.resultadogeral,.tmtimagem,.tmtdados{ width: 210mm; min-height: 257mm; display: flex; justify-content: center; align-items: center; height: auto; }
                    .full-image { max-width: 100%; max-height: 100vh; object-fit: contain; }
                    .capa{display: flex;flex-direction: column;align-items: center;justify-content: center;box-sizing: border-box; width: 210mm; height: 257mm;}
                    h1{font-size: 120px; text-align: center; margin-top: 0;}
                    .textonormal{margin-bottom: 0; align-self:start; margin-left: 10%; font-size: 20px;}
                    .resultadogeral,.tmtdados{justify-content: start; width: 210mm; flex-direction: column; margin-bottom: 20mm; }
                    h2{font-size: 70px; margin-top: 8%;margin-bottom: 4%;}
                    hr{border-radius: 2px; background-color:#05A483 ; width: 166mm; margin:0px; margin-bottom: 4%; height: 8px}
                    h3{margin: 0; font-size: 30px;}
                    p{font-size: 20px; margin: 0;}
                    .resultadogeral{background-color:pink; height: 257mm;}
                </style>
            </head>
            <body>
                <div class="capa">
                    <h1>TMTrilhas<br/></h1>
                    <p class="textonormal" id="nomedopaciente">Paciente: ${TestSession.patientName} - ${TestSession.patientAge} anos</p>
                    <p class="textonormal" id="identificador">Identificador: ${TestSession.patientIdentifier}</p>
                    <p class="textonormal" id="nomedoprofissional">Profissional: ${TestSession.professionalName} - ${TestSession.professionalIdentifier}</p>
                    <p class="textonormal" id="data"> Data: $date </p>
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-A</h2>
                    <div>
                        <h3>Métricas</h3>
                        <hr>
                        <p style="font-weight: 700;" >${resultA.getFormattedTotalTime()}</p>
                        <p>${resultA.getLiftsCount()} </p>
                        <p>${resultA.getLiftsTotalTime()} </p>
                        <p>${resultA.getTotalTimeWithoutLifts()} </p>
                        <p>${resultA.getCorrectConnectionsCount()} </p>
                        <p>${resultA.getIncorrectConnectionsCount()} </p>
                        
                        <br><br>
                        
                        <h3> Detalhes</h3>
                        <hr>
                        <p> ${resultA.getDetails().replace("\n", "<br>")}</p>

                    </div>
                </div>
                <div class="tmtimagem">
                    <img src="data:image/png;base64,${TestSession.imageTMTA}" class="full-image" />
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-B</h2>
                    <div>
                        <h3>Métricas</h3>
                        <hr>
                        <p style="font-weight: 700;" >${resultB.getFormattedTotalTime()}</p>
                        <p>${resultB.getLiftsCount()} </p>
                        <p>${resultB.getLiftsTotalTime()} </p>
                        <p>${resultB.getTotalTimeWithoutLifts()} </p>
                        <p>${resultB.getCorrectConnectionsCount()} </p>
                        <p>${resultB.getIncorrectConnectionsCount()} </p>
                         
                         <br><br>
                        
                        <h3> Detalhes</h3>
                        <hr>
                        <p> ${resultB.getDetails().replace("\n", "<br>")}</p>
                    </div>
                </div>
                 <div class="tmtimagem">
                    <img src="data:image/png;base64,${TestSession.imageTMTB}" class="full-image" />
                </div>
            </body>
            </html>
        """.trimIndent()
    }

}
