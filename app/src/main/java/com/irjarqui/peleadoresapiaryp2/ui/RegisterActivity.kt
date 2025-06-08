package com.irjarqui.peleadoresapiaryp2.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.irjarqui.peleadoresapiaryp2.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.etEmailRegister)
        val passEt = findViewById<EditText>(R.id.etPasswordRegister)
        val confirmEt = findViewById<EditText>(R.id.etConfirmPassword)
        val registerBtn = findViewById<Button>(R.id.btnRegisterConfirm)
        val btnBack = findViewById<Button>(R.id.btnBack)

        registerBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val pass = passEt.text.toString().trim()
            val confirm = confirmEt.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.correo_invalido), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(this, R.string.seis_caracteres, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirm) {
                Toast.makeText(this,
                    getString(R.string.las_contrase_as_no_coinciden), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                            getString(R.string.cuenta_creada_con_exito), Toast.LENGTH_LONG).show()
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
