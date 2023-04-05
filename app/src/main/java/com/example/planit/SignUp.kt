package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val siAccount = findViewById<TextView>(R.id.login_view)

        siAccount.setOnClickListener{
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val email = findViewById<EditText>(R.id.email)
        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)
        val cnfpass = findViewById<EditText>(R.id.confermapassword)
        val btnSignUp = findViewById<Button>(R.id.signupbtn)

        btnSignUp.setOnClickListener {
            val strUser: String = user.text.toString()
            val strPass: String = pass.text.toString()
            val strcnfPass: String = cnfpass.text.toString()
            val stremail: String = email.text.toString()
            if(strPass.equals(strcnfPass)) {
                if(stremail.contains('@') && stremail.contains('.')){
                    if (!strUser.contains('@') && !strPass.contains(' ') && strPass.isNotEmpty() && strUser.isNotEmpty()) {
                        val intentLogin = Intent(this, ActivityMain::class.java)
                        startActivity(intentLogin)
                    } else {
                        Toast.makeText(this, "I valori inseriti non sono corretti!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "L'email non è ammissibile!", Toast.LENGTH_SHORT)
                        .show()
                }

            } else{
                Toast.makeText(this, "La password confermata non è corretta!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
