package com.example.quizapp




import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var optionA: Button
    private lateinit var optionB: Button
    private lateinit var optionC: Button
    private lateinit var optionD: Button
    private lateinit var timerText: TextView
    private lateinit var circularTimer: ProgressBar

    private val questions=listOf(
        Question("What is the capital of Pakistan?", listOf("Karachi", "Islamabad", "Lahore", "Peshawar"), "Islamabad"),
        Question("Which language is used for Android?", listOf("Python", "Swift", "Kotlin", "JavaScript"), "Kotlin"),
        Question("What is 5 + 7?", listOf("10", "11", "12", "13"), "12"),
        Question("Which is the largest planet in our solar system?", listOf("Earth", "Venus", "Jupiter", "Mars"), "Jupiter"),
        Question("Who was the first president of the United States?", listOf("George Washington", "Abraham Lincoln", "Thomas Jefferson", "John Adams"), "George Washington")
    )

    private var currentIndex=0
    private var score=0
    private var selected= false
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText=findViewById(R.id.questionText)
        optionA=findViewById(R.id.optionA)
        optionB=findViewById(R.id.optionB)
        optionC=findViewById(R.id.optionC)
        optionD=findViewById(R.id.optionD)
        timerText=findViewById(R.id.timerText)
        circularTimer=findViewById(R.id.circularTimer)
        loadQuestion()
    }

    private fun loadQuestion(){
        if (currentIndex>=questions.size){
            showResult()
            return
        }

        val current=questions[currentIndex]
        questionText.text=current.text
        optionA.text=current.options[0]
        optionB.text=current.options[1]
        optionC.text=current.options[2]
        optionD.text=current.options[3]

        resetOptions()
        selected=false

        startTimer()
        setOptionClickListeners()
    }

    private fun setOptionClickListeners(){
        val current=questions[currentIndex]
        val options=listOf(optionA, optionB, optionC, optionD)

        options.forEach{button ->
            button.setOnClickListener {
                if(selected)return@setOnClickListener
                selected = true
                countDownTimer.cancel()

                highlightSelected(button)
                disableOptions()

                val answer=button.text.toString()
                if (answer==current.correctAnswer) {
                    score++
                }

                // Move to next question after a delay
                button.postDelayed({
                    currentIndex++
                    loadQuestion()
                },1000)
            }
        }
    }

    private fun highlightSelected(button: Button){
        val allOptions=listOf(optionA, optionB, optionC, optionD)
        allOptions.forEach{
            it.setBackgroundColor(ContextCompat.getColor(this,R.color.smoke_grey))
        }
        button.setBackgroundColor(ContextCompat.getColor(this,R.color.light_purple))
    }

    private fun disableOptions(){
        optionA.isEnabled=false
        optionB.isEnabled=false
        optionC.isEnabled=false
        optionD.isEnabled=false
    }

    private fun resetOptions(){
        val allOptions=listOf(optionA, optionB, optionC, optionD)
        allOptions.forEach{
            it.setBackgroundColor(ContextCompat.getColor(this,R.color.smoke_grey))
            it.isEnabled=true
        }
    }

    private fun startTimer(){
        circularTimer.progress=100

        countDownTimer=object:CountDownTimer(10000,100) {
            override fun onTick(millisUntilFinished: Long){
                val seconds=millisUntilFinished/1000+1
                timerText.text="${seconds}"
                circularTimer.progress=(millisUntilFinished*100/10000).toInt()
            }

            override fun onFinish(){
                if (!selected){
                    currentIndex++
                    loadQuestion()
                }
            }
        }
        countDownTimer.start()
    }

    private fun showResult(){
        val intent = Intent(this,ResultActivity::class.java)
        intent.putExtra("score",score)
        intent.putExtra("total",questions.size)
        startActivity(intent)
        finish()
    }
}
