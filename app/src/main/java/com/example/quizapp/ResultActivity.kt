package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
         ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v,insets ->
              val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val scoreText=findViewById<TextView>(R.id.scoreText)
        val circularTimer=findViewById<ProgressBar>(R.id.circularTimer)

        val score=intent.getIntExtra("score", 0)
        circularTimer.progress = score * 20

        scoreText.text="$score/5"

        val restartButton = findViewById<Button>(R.id.restart)
        restartButton.setOnClickListener {
            val intent=Intent(this, QuizActivity::class.java).apply {
                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }


    }
    override fun onBackPressed(){
        finishAffinity() // This closes all activities and exits the app
        super.onBackPressed()
    }

}