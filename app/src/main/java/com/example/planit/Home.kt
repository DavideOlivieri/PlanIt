package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
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

    fun addButton(){
        val inflater = LayoutInflater.from(this)
        val nome = intent.getStringExtra("Nome calendario")
        val buttonLayout = inflater.inflate(R.layout.calendar_button, null)
        val button = buttonLayout.findViewById<Button>(R.id.button)
        button.setText(nome)
        val relative = findViewById<RelativeLayout>(R.id.linearlayout)
        relative.addView(buttonLayout)
    }




/*
    @SuppressLint("ResourceAsColor")
    fun addButton(){
        var button: Button
        val nome = intent.getStringExtra("Nome calendario")
        val relative = findViewById<RelativeLayout>(R.id.linearlayout)
        button = Button(this)
        button.setTextColor(R.color.black)
        button.setText(nome)
        button.setBackgroundColor(R.color.white)  // non funziona colore

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        )
        params.addRule(RelativeLayout.BELOW)
        relative.addView(button,params)
    }
*/
    }




