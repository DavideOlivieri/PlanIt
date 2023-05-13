package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import roomData.User
import roomData.UserDatabase

class Info_Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_account)

        // inizializzazione variabili
        val titolo = intent.getStringExtra("titolo")


        val userDao = UserDatabase.getInstance(application).dao()


    }
}