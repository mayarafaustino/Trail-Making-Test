package com.example.tmt

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

class ResultsScreenActivity : AppCompatActivity()
{

    private val ScreenCapture = ScreenCapture()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_results_screen)

        val pdfGenerator = PdfGenerator()

        // Referência para os TextViews do TMT-A
        val textTempoTotalA = findViewById<TextView>(R.id.textView19)
        val textTempoToqueLevantadoA = findViewById<TextView>(R.id.textView20)
        val textToquesLevantadosA = findViewById<TextView>(R.id.textViewTMTATouches)
        val textConexoesCorretasA = findViewById<TextView>(R.id.textViewTMTAConexoesCorretas)
        val textConexoesIncorretasA = findViewById<TextView>(R.id.textViewTMTAConexoesIncorretas)
        val textDetalheTmtA = findViewById<TextView>(R.id.textView55)

        // Referência para os TextViews do TMT-B
        val textTempoTotalB = findViewById<TextView>(R.id.textView23)
        val textTempoToqueLevantadoB = findViewById<TextView>(R.id.textView21)
        val textToquesLevantadosB = findViewById<TextView>(R.id.textViewTMTBTouches)
        val textConexoesCorretasB = findViewById<TextView>(R.id.textViewTMTBConexoesCorretas)
        val textConexoesIncorretasB = findViewById<TextView>(R.id.textViewTMTBConexoesIncorretas)
        val textDetalheTmtB = findViewById<TextView>(R.id.textView56)

        val btnToggleA: ImageButton = findViewById(R.id.buttonExpandA)
        val btnToggleB: ImageButton = findViewById(R.id.buttonExpandB)
        val expandableLayoutA: LinearLayout = findViewById(R.id.expandLayoutA)
        val expandableLayoutB: LinearLayout = findViewById(R.id.expandLayoutB)
        val btnBackToHome: Button = findViewById(R.id.buttonBackToHome)


        // Configura o clique do botão para expandir e recolher o LinearLayoutA
        btnToggleA.setOnClickListener {
            if (expandableLayoutA.visibility == View.GONE) {
                // Expande
                expandableLayoutA.visibility = View.VISIBLE
                btnToggleA.setImageResource(R.drawable.up_arrow)
            } else {
                // Recolhe
                expandableLayoutA.visibility = View.GONE
                btnToggleA.setImageResource(R.drawable.drop_arrow)
            }
        }

        // Configura o clique do botão para expandir e recolher o LinearLayoutB

        btnToggleB.setOnClickListener {
            if (expandableLayoutB.visibility == View.GONE) {
                // Expande
                expandableLayoutB.visibility = View.VISIBLE
                btnToggleB.setImageResource(R.drawable.up_arrow)
            } else {
                // Recolhe
                expandableLayoutB.visibility = View.GONE
                btnToggleB.setImageResource(R.drawable.drop_arrow)
            }
        }

        //val imageView = findViewById<ImageView>(R.id.image_preview)
        val btnPDF = findViewById<ImageButton>(R.id.buttonDownload)

        //val base64Image = intent.getStringExtra("imagemBase64")
        //val bitmap = base64ToBitmap(base64Image)
        //imageView.setImageBitmap(bitmap)
        val patientName            = TestSession.patientName
        val patientAge             = TestSession.patientAge
        val patientIdentifier      = TestSession.patientIdentifier
        val professionalName       = TestSession.professionalName
        val professionalIdentifier = TestSession.professionalIdentifier
        val imageTMTA              = TestSession.imageTMTA
        val imageTMTB              = TestSession.imageTMTB


        TestSession.results.forEach { result ->
            if (result.getTestType() == TmtType.TMT_A)
            {
                // Preencher dados do TMT-A
                textTempoTotalA.text          = result.getFormattedTotalTime()
                textTempoToqueLevantadoA.text = result.getTotalTouchUpTime()
                textToquesLevantadosA.text    = result.getTouchUpCount()
                textConexoesCorretasA.text    = result.getCorrectConnectionsCount()
                textConexoesIncorretasA.text  = result.getIncorrectConnectionsCount()
                textDetalheTmtA.text          = result.getDetails()
            }
            else if (result.getTestType() == TmtType.TMT_B)
            {
                // Preencher dados do TMT-B
                textTempoTotalB.text          = result.getFormattedTotalTime()
                textTempoToqueLevantadoB.text = result.getTotalTouchUpTime()
                textToquesLevantadosB.text    = result.getTouchUpCount()
                textConexoesCorretasB.text    = result.getCorrectConnectionsCount()
                textConexoesIncorretasB.text  = result.getIncorrectConnectionsCount()
                textDetalheTmtB.text          = result.getDetails()
            }
        }

        btnPDF.setOnClickListener {

            val html = pdfGenerator.createHtmlPDF(patientName, patientAge, patientIdentifier, professionalName, professionalIdentifier, imageTMTA, imageTMTB )


            pdfGenerator.generatePdf(this, html, patientName, patientIdentifier) { file ->
                Toast.makeText(this, "PDF salvo em: ${file?.path}", Toast.LENGTH_LONG).show()
            }

        }

        btnBackToHome.setOnClickListener {
            // Criar o AlertDialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmar saída")
            builder.setMessage("Você tem certeza que deseja sair?")

            builder.setPositiveButton("Sim") { dialog, which ->
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            builder.setNegativeButton("Não") { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
        }

/*

         private val sharedViewModel: SharedViewModel by viewModels()
        //
        val bitmap = screenCapture.captureScreen(this)

        //
        val base64Bitmap = screenCapture.bitmapToBase64(bitmap)
        sharedViewModel.imageTMTA = base64Bitmap
        sharedViewModel.imageTMTB = base64Bitmap
*/


    }

}