package com.irjarqui.peleadoresapiaryp2.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.irjarqui.peleadoresapiaryp2.R

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val emailEt = findViewById<EditText>(R.id.etEmailReset)
        val btnReset = findViewById<Button>(R.id.btnSendReset)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnReset.setOnClickListener {
            val email = emailEt.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.correo_invalido, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                            getString(R.string.correo_enviado_revisa_tu_bandeja), Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
