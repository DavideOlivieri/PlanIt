package com.example.planit

import android.annotation.SuppressLint
import android.os.Bundle
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
        val data = findViewById<TextView>(R.id.Data)
        val calendario = findViewById<TextView>(R.id.nome_calendario)
        data.text = selectedDate
        data.setText(selectedDate)
        calendario.text = titolo_cal
        calendario.setText(titolo_cal)

        val fragment = Fragment_evento()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, fragment)
            .commit()
    }
}