package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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

        // Aggiungi un nuovo Calendario (schermata scelta dati calendario)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddCalendar::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        val isOk = intent.getBooleanExtra("isOk",false)

        // Aggiungi nuovi calendari
        if (isOk) {
                addButton()
        }
    }



    @SuppressLint("ResourceAsColor")
    fun addButton(){
        var button: Button
        val nome = intent.getStringExtra("Nome calendario")
        val linear = findViewById<LinearLayout>(R.id.linearlayout)
        button = Button(this)
        button.setTextColor(R.color.black)
        button.setText(nome)
        button.setBackgroundColor(R.color.white)  // non funziona colore
        linear.addView(button)
    }

    }




