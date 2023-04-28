package com.example.planit


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity



//Activity necessaria per aggiungere un nuovo calendario (nome del calendario)
class AddCalendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.calendario.R.layout.activity_addcalendar)


        //passaggio alla schermata dei calendari
        val sicalendario = findViewById<Button>(com.example.calendario.R.id.btn_crea)

        sicalendario.setOnClickListener{
            val edit = findViewById<EditText>(com.example.calendario.R.id.editText)
            val result = edit.text.toString()
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }

    }
}