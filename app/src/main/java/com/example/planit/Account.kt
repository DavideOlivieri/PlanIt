package com.example.planit

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_account)

        // inizializzazione variabili
        val email = findViewById<TextView>(R.id.email)
        val imm = findViewById<ImageView>(R.id.imaccount)
        val us = findViewById<TextView>(R.id.username)
        val btnModifica = findViewById<Button>(R.id.modbtn)
        val username = intent.getStringExtra("Username")


        val userDao = UserDatabase.getInstance(application).dao()
        val user: User = userDao.selectUser(username)

        email.setText(user.email)
        us.setText(user.user)

        btnModifica.setOnClickListener {
            val intent = Intent(this, ModAccount::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }
    }
}
