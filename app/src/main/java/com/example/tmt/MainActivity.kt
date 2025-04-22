package com.example.tmt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnTestFinishedListener
{
    private lateinit var drawingView: DrawingView
    private lateinit var btnNext: Button
    private lateinit var footerArea: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tmtTypeString = intent.getStringExtra("TMT_TYPE")
        val tmtType = if (tmtTypeString != null) TmtType.valueOf(tmtTypeString) else TmtType.TMT_A

        drawingView = findViewById(R.id.drawing_view)
        btnNext = findViewById(R.id.btnNext)
        footerArea = findViewById(R.id.footerArea)

        drawingView.setTmtType(tmtType)
        drawingView.setOnTestFinishedListener(this)

//        btnNext.visibility = View.GONE // Esconde no início
        footerArea.visibility = View.GONE // Esconde no início

        setTextButton(tmtType)

        btnNext.setOnClickListener {

            //se for sample, passa para o próximo teste
            if(tmtType.isSample())
            {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TMT_TYPE", getNextTmt(tmtType).name)
                startActivity(intent)
                finish()

            }
            else if (tmtType == TmtType.TMT_A)
            {
                val intent = Intent(this, TMTBInformationActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (tmtType == TmtType.TMT_B)
            {
                val intent = Intent(this, TestCompletedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onTestFinished(results: TestResults)
    {
        setFooterAreaVisibility()
//        btnNext.visibility = View.VISIBLE
//        footerArea.visibility = View.VISIBLE

        if (results != TestResults.EMPTY)
            TestSession.results.add(results)
    }

    private fun getNextTmt(currentTmt: TmtType): TmtType
    {
        return when (currentTmt) {
            TmtType.TMT_A_SAMPLE -> TmtType.TMT_A
            TmtType.TMT_B_SAMPLE -> TmtType.TMT_B
            else -> TmtType.TMT_A_SAMPLE
        }
    }

    private fun setTextButton(tmtType: TmtType)
    {
        if(tmtType.isSample())
            btnNext.text = getString(R.string.start_test)
        else if (tmtType == TmtType.TMT_A)
            btnNext.text = getString(R.string.go_to_next_tmt)
    }

    private fun setFooterAreaVisibility()
    {
        footerArea.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
//        btnNext.visibility = View.VISIBLE
    }

}


