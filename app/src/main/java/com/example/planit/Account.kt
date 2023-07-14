package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import roomData.User
import roomData.UserDatabase

class Account : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_account)

        // inizializzazione variabili
        val email = findViewById<TextView>(R.id.email)
        val us = findViewById<TextView>(R.id.username)
        val btnModifica = findViewById<Button>(R.id.modbtn)
        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val username = intent.getStringExtra("Username")

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Calendario::class.java)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        val userDao = UserDatabase.getInstance(application).dao()
        val user: User = userDao.selectUser(username)

        email.setText(user.email)
        us.setText(user.user)

        btnModifica.setOnClickListener {
            val intent = Intent(this, ModAccount::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
