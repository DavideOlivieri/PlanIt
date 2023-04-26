package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //inizializzazione variabili
        val account = findViewById<ImageView>(R.id.Account)
        val btnAdd = findViewById<Button>(R.id.Aggiungi_Calendario)
        val username = intent.getStringExtra("Username")

        // passaggio alla schermata di ModAccount
        account.setOnClickListener {
            val intent = Intent(this, ModAccount::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        // Aggiungi un nuovo Calendario
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddCalendar::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }
    }
}