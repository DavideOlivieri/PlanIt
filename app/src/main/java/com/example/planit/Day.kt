package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R

class Day: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        val selectedDate = intent.getStringExtra("data_selezionata")
        val titolo_cal = intent.getStringExtra("titolo_cal")
        val Btn_agg = findViewById<Button>(R.id.Aggiungi_Calendario)
        val data = findViewById<TextView>(R.id.Data)
        val calendario = findViewById<TextView>(R.id.Nome_calendario)
        data.setText(selectedDate)
        calendario.setText(titolo_cal)

        Btn_agg.setOnClickListener {
            val intent = Intent(this, Event::class.java)
            //intent.putExtra("Date", selectedDate)
            startActivity(intent)
        }

    }
}