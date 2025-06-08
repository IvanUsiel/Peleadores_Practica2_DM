package com.irjarqui.peleadoresapiaryp2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.irjarqui.peleadoresapiaryp2.R

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.etEmail)
        val passEt = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signupTv = findViewById<TextView>(R.id.tvSignup)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val pass = passEt.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.ese_correo_no_es_valido), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(this, getString(R.string.seis_caracteres), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                            getString(R.string.bienvenido_campeon), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        signupTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val forgotPassTv = findViewById<TextView>(R.id.tvForgotPassword)

        forgotPassTv.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
