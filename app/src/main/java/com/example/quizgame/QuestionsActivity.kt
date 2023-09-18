package com.example.quizgame

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quizgame.databinding.ActivityQuestionsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding
    private lateinit var countDown: CountDownTimer

    private val database = Firebase.database
    private val myRef = database.reference.child("questions")
    private var questionTotal = 0
    private var questionNumber = 1
    private var correctCount = 0
    private var wrongCount = 0
    private var correctAnswer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showQuestion()

        binding.tvA.setOnClickListener {
            if (correctAnswer == "a") {
                binding.tvA.setBackgroundColor(Color.GREEN)
                correctCount++
                binding.tvCorrect.text = correctCount.toString()
            } else {
                binding.tvA.setBackgroundColor(Color.RED)
                wrongCount++
                binding.tvWrong.text = wrongCount.toString()
                showCorrectAnswer()
            }
            stopCountDown()
            disableChooseAnswers()
        }

        binding.tvB.setOnClickListener {
            if (correctAnswer == "b") {
                binding.tvB.setBackgroundColor(Color.GREEN)
                correctCount++
                binding.tvCorrect.text = correctCount.toString()
            } else {
                binding.tvB.setBackgroundColor(Color.RED)
                wrongCount++
                binding.tvWrong.text = wrongCount.toString()
                showCorrectAnswer()
            }
            stopCountDown()
            disableChooseAnswers()
        }

        binding.tvC.setOnClickListener {
            if (correctAnswer == "c") {
                binding.tvC.setBackgroundColor(Color.GREEN)
                correctCount++
                binding.tvCorrect.text = correctCount.toString()
            } else {
                binding.tvC.setBackgroundColor(Color.RED)
                wrongCount++
                binding.tvWrong.text = wrongCount.toString()
                showCorrectAnswer()
            }
            stopCountDown()
            disableChooseAnswers()
        }

        binding.tvD.setOnClickListener {
            if (correctAnswer == "d") {
                binding.tvD.setBackgroundColor(Color.GREEN)
                correctCount++
                binding.tvCorrect.text = correctCount.toString()
            } else {
                binding.tvD.setBackgroundColor(Color.RED)
                wrongCount++
                binding.tvWrong.text = wrongCount.toString()
                showCorrectAnswer()
            }
            stopCountDown()
            disableChooseAnswers()
        }

        binding.btnFinish.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("correct", correctCount)
            intent.putExtra("wrong", wrongCount)
            startActivity(intent)
            finish()
        }

        binding.btnNext.setOnClickListener {
            showQuestion()
        }
    }

    private fun showQuestion() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionTotal = snapshot.childrenCount.toInt()

                if (questionNumber <= questionTotal) {
                    resetQuestionUI()
                    val question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    val answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    val answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    val answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    val answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    Log.d("total", questionTotal.toString())
                    Log.d("question", question)
                    Log.d("A", answerA)
                    Log.d("correct", correctAnswer)

                    binding.tvQuestion.text = question
                    binding.tvA.text = answerA
                    binding.tvB.text = answerB
                    binding.tvC.text = answerC
                    binding.tvD.text = answerD

                    binding.progressBarQuestion.visibility = View.INVISIBLE
                    binding.linearLayoutQuestion.visibility = View.VISIBLE
                    binding.linearLayoutBtn.visibility = View.VISIBLE
                    binding.linearLayoutStatus.visibility = View.VISIBLE
                } else {
                    showDialog()
                }

                questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun disableChooseAnswers() {
        binding.tvA.isClickable = false
        binding.tvB.isClickable = false
        binding.tvC.isClickable = false
        binding.tvD.isClickable = false
    }

    private fun showCorrectAnswer() {
        when (correctAnswer) {
            "a" -> binding.tvA.setBackgroundColor(Color.GREEN)
            "b" -> binding.tvB.setBackgroundColor(Color.GREEN)
            "c" -> binding.tvC.setBackgroundColor(Color.GREEN)
            "d" -> binding.tvD.setBackgroundColor(Color.GREEN)
        }
    }

    private fun resetQuestionUI() {
        startCountDown()
        binding.tvA.setBackgroundColor(Color.WHITE)
        binding.tvB.setBackgroundColor(Color.WHITE)
        binding.tvC.setBackgroundColor(Color.WHITE)
        binding.tvD.setBackgroundColor(Color.WHITE)

        binding.tvA.isClickable = true
        binding.tvB.isClickable = true
        binding.tvC.isClickable = true
        binding.tvD.isClickable = true
    }

    private fun startCountDown() {
        countDown = object : CountDownTimer(60000, 1000) {
            override fun onTick(p0: Long) {
                binding.tvTime.text = (p0 / 1000).toString()
            }

            override fun onFinish() {
                disableChooseAnswers()
                binding.tvQuestion.text = getString(R.string.times_up)
            }

        }.start()
    }

    private fun stopCountDown() {
        countDown.cancel()
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("Quiz Game")
            .setMessage(R.string.dialog_message)
            .setPositiveButton("SEE RESULT") { _: DialogInterface, _ ->
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("correct", correctCount)
                intent.putExtra("wrong", wrongCount)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("PLAY AGAIN") { _: DialogInterface, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .create()
            .show()
    }
}