package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        //inizializzazione variabili
        val noAccount = findViewById<TextView>(R.id.sign_up_view)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)

        // passaggio alla schermata di signUp
        noAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        // controllo valori inseriti
        btnLogin.setOnClickListener {
            val strUser: String = user.text.toString()
            val strPass: String = pass.text.toString()
            if (strPass.isNotEmpty()) {
                if (strUser.isNotEmpty()) {
                        if (strUser.length < 15) {
                            val intentLogin = Intent(this, ActivityMain::class.java)
                            startActivity(intentLogin)
                        } else {
                            Toast.makeText(this,"I valori inseriti non sono corretti!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this,"Inserire lo User!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Inserire la Password!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}


