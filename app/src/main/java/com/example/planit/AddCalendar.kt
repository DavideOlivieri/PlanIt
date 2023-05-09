package com.example.planit


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import roomData.Calendar
import roomData.UserDatabase
import roomData.User_Calendar_id


//Activity necessaria per aggiungere un nuovo calendario (nome del calendario)
class AddCalendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.calendario.R.layout.activity_addcalendar)


        //passaggio alla schermata dei calendari
        val sicalendario = findViewById<Button>(com.example.calendario.R.id.btn_crea)

        val userDao = UserDatabase.getInstance(application).dao()
        val username = intent.getStringExtra("Username")

        sicalendario.setOnClickListener{
            val edit = findViewById<EditText>(com.example.calendario.R.id.editText)
            val nome = edit.text.toString()
            var color = "Nessun colore"
            val intent = Intent(this,Home::class.java)
            /*
            fun isOk():Boolean {
                if (nome.isNotEmpty()) {
                    intent.putExtra("Nome calendario", nome)
                    val newCalendar = Calendar(nome, color)
                    userDao.insertCalendar(newCalendar)
                    return true
                }
                else return false
            }
            intent.putExtra("isOk",isOk())
            startActivity(intent)

            if (nome.isNotEmpty()) {
                intent.putExtra("Nome calendario", nome)
            }*/
            if(nome.isNotEmpty()) {
                val newCalendar = Calendar(nome, color)
                userDao.insertCalendar(newCalendar)
                val idCalendar = userDao.getIdFromCalendar(nome)
                val newAssoc = User_Calendar_id(username, idCalendar)
                userDao.insertUserCalendarId(newAssoc)
                intent.putExtra("Username", username)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Inserisci un nome per il calendario!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}