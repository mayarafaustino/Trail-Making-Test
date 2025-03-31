package com.example.tmt
import android.content.Context
import android.os.Environment
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.File

class PdfGenerator {

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
    //fun createHtmlPDF(imageTMTABase64: String, imageTMTBBase64: String, itens: List<String>): String {
    fun createHtmlPDF(patientName: String? = null, patientAge: Int? = null, patientIdentifier: String? = null, professionalName: String? = null, professionalIdentifier: String? = null, imageTMTABase64: String? = null, imageTMTBBase64: String? = null ): String {

        /*val paragrafosHtml = itens.joinToString("\n") { item ->
            """<p>$item</p>"""
        }*/

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    @page { size: A4; margin: 0; }
                    html, body { width: 100%; height: 100%; margin: 0; padding: 0; font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif ;}
                    .capa,.resultadogeral,.tmtimagem,.tmtdados{ width: 100vw; height: 100vh; display: flex; justify-content: center; align-items: center; }
                    .full-image { max-width: 100%; max-height: 100vh; object-fit: contain; }
                    .capa{flex-direction: column; width: 210mm; /* Largura A4 */height: 297mm;}
                    h1{font-size: 160px; text-align: center; margin-top: 0;}
                    .textonormal{margin-bottom: 0; align-self:start; margin-left: 10%; font-size: 20px;}
                    .resultadogeral,.tmtdados{justify-content: start; width: 210mm; /* Largura A4 */height: 297mm;flex-direction: column;}
                    h2{font-size: 70px; margin-top: 8%;margin-bottom: 4%;}
                    hr{border-style:solid; width: 166mm; margin:0px;border-color: #05A483 ; margin-bottom: 4%;}
                    h3{margin: 0; font-size: 40px;}
                    p{font-size: 20px; margin: 0;}
                    .tmtdados{ background-color: rgb(253, 182, 232);}
                </style>
            </head>
            <body>
                <div class="capa">
                    <h1>Trial<br/>Making<br/>Test<br/></h1>
                    <p class="textonormal" id="nomedopaciente">Paciente: $patientName - $patientAge anos</p>
                    <p class="textonormal" id="identificador">Identificador:$patientIdentifier</p>
                    <p class="textonormal" id="nomedoprofissional">Profissional: $professionalName - $professionalIdentifier</p>
                </div>
                <div class="resultadogeral">
                    <h2 class="titulo">Resultado geral</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;">Tempo total : 300s</p>
                        <p >- Tempo líquido: 200s </p>
                        <p >- Tempo em pausa: 100s</p>
                        <br><br>
                        <p style="font-weight: 700;">Números:</p>
                        <p >- Conexões corretas: </p>
                        <p >- Conexões erradas: </p>
                    </div>
                </div>
                <div class="tmtimagem">
                    <img src="data:image/png;base64,$imageTMTABase64" class="full-image" />
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-A</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;" >Tempo total : 300s</p>
                        <p >- Tempo líquido: 200s </p>
                        <p >- Tempo em pausa: 100s</p>
                        <br><br>
                        <p style="font-weight: 700;" >Números:</p>
                        <p >- Conexões corretas: </p>
                        <p>paragrafosHtml </p>
                        <p >- Conexões erradas: </p>
                        <br><br>
                        <p style="font-weight: 700;" >Tempo das conexões:</p>
                        <br><br>
                        <p style="font-weight: 700;" >Tempos das pausas entre conexões:</p>
                        <br><br>
                        <p style="font-weight: 700;" >Conexões erradas:</p>
                    </div>
                </div>
                <div class="tmtimagem">
                    <img src="data:image/png;base64,$imageTMTBBase64" class="full-image" />
                </div>
                <div class="tmtdados">
                    <h2 class="titulo">Resultados TMT-B</h2>
                    <div>
                        <h3>Tempos</h3>
                        <hr>
                        <p style="font-weight: 700;" >Tempo total : 300s</p>
                        <p >- Tempo líquido: 200s </p>
                        <p >- Tempo em pausa: 100s</p>
                        <br><br>
                        <p style="font-weight: 700;" >Números:</p>
                        <p >- Conexões corretas: </p>
                        <p >- Conexões erradas: </p>
                        <br><br>
                        <p style="font-weight: 700;" >Tempo das conexões:</p>
                        <br><br>
                        <p style="font-weight: 700;" >Tempos das pausas entre conexões:</p>
                        <br><br>
                        <p style="font-weight: 700;" >Conexões erradas:</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
