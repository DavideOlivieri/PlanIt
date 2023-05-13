package com.example.planit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R

class Day: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        val selectedDate = intent.getStringExtra("data_selezionata")
        val titolo_cal = intent.getStringExtra("titolo_cal")
        val data = findViewById<TextView>(R.id.textView)
        data.text = selectedDate
        data.setText(selectedDate)
    }
}