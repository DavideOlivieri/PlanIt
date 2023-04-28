package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
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

/*
        // Aggiungi nuovi calendari
        btnAdd.setOnClickListener {
          fun onClick(view: View){
              addButton()
          }
        }
*/

    }

/*
    lateinit var button: Button
    fun addButton(){
        val constraint = findViewById<ConstraintLayout>(R.id.layout1)
        button = Button(this)
        button.setText("Calendario_1")
        button.
        constraint.addView(button)
    }
*/
}