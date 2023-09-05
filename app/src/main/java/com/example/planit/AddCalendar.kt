package com.example.planit


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.Calendar
import roomData.UserDatabase
import roomData.User_Calendar_id


//Activity necessaria per aggiungere un nuovo calendario (nome del calendario)
class AddCalendar : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcalendar)


        //passaggio alla schermata dei calendari
        val sicalendario = findViewById<Button>(R.id.btn_crea)
        val calendarioEsistente = findViewById<Button>(R.id.btn_partecipa)

        // inizializzazione variabili
        val userDao = UserDatabase.getInstance(application).dao()
        val username = intent.getStringExtra("Username")

        // collegamento con firebase
        val database = Firebase.database
        val myRef = database.getReference("calendars")

        // bottone indietro
        val btnIndietro = findViewById<Button>(R.id.Indietro)
        btnIndietro.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        // funzione che si attiva se se vuole creare un calendario
        sicalendario.setOnClickListener{
            val edit = findViewById<EditText>(R.id.editText)
            val nome = edit.text.toString()
            val color = "Nessun colore"
            val codiceIngresso = username + nome
            val intent = Intent(this,Home::class.java)


            if(nome.isNotEmpty()) {
                val newCalendar = Calendar(nome, color, codiceIngresso)
                userDao.insertCalendar(newCalendar)
                val idCalendar = userDao.getIdFromCalendar(nome,codiceIngresso)
                val newAssoc = User_Calendar_id(username, idCalendar,"1")
                userDao.insertUserCalendarId(newAssoc)
                intent.putExtra("Username", username)
                val newCalendarRef = myRef.push()
                newCalendarRef.setValue(newCalendar)

                val ref = database.getReference("assocs")
                val newAssocRef = ref.push()
                newAssocRef.setValue(newAssoc)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Inserisci un nome per il calendario!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // funzione che si attiva se si vuole partecipare a un calendario
        calendarioEsistente.setOnClickListener{
            val edit = findViewById<EditText>(R.id.editText_esistente)
            val codiceCalendario = edit.text.toString()
            val intent = Intent(this,Home::class.java)


            if(codiceCalendario.isNotEmpty()) {

                // controllo necessario per evitare errori
                if (userDao.selectIdbyCodice(codiceCalendario)!=null) {
                    val newAssoc = User_Calendar_id(username, userDao.selectIdbyCodice(codiceCalendario),"0")
                    userDao.insertUserCalendarId(newAssoc)
                    intent.putExtra("Username", username)

                    val ref = database.getReference("assocs")
                    val newAssocRef = ref.push()
                    newAssocRef.setValue(newAssoc)

                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Il codice del calendario non è corretto!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this, "Inserisci il codice del calendario!", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }
}