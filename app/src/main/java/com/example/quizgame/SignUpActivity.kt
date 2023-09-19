package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.editTextSignUpEmail.text.toString()
            val password = binding.editTextSignUpPassword.text.toString()

            signUp(email, password)
        }
    }

    private fun signUp(email: String, password: String) {
        binding.btnSignUp.isClickable = false

        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Sign up successfully", Toast.LENGTH_SHORT).show()
                finish()
                binding.btnSignUp.isClickable = true
            } else {
                Toast.makeText(applicationContext, "Error sign up!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}