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
        val noAccount = findViewById<TextView>(R.id.sign_up_view)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)

        noAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val strUser: String = user.text.toString()
            val strPass: String = pass.text.toString()
            if (!strUser.contains('@') && !strPass.contains(' ') && strPass.isNotEmpty() && strUser.isNotEmpty()) {
                val intentLogin = Intent(this, ActivityMain::class.java)
                startActivity(intentLogin)
            }
            else{
                Toast.makeText(this,"I valori inseriti non sono corretti!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}


