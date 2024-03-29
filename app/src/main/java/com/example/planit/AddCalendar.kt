package com.example.planit


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        setContentView(com.example.calendario.R.layout.activity_addcalendar)


        //passaggio alla schermata dei calendari
        val sicalendario = findViewById<Button>(com.example.calendario.R.id.btn_crea)
        val calendario_esistente = findViewById<Button>(com.example.calendario.R.id.btn_partecipa)

        val userDao = UserDatabase.getInstance(application).dao()
        val username = intent.getStringExtra("Username")

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("calendars")

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        sicalendario.setOnClickListener{
            val edit = findViewById<EditText>(com.example.calendario.R.id.editText)
            val nome = edit.text.toString()
            var color = "Nessun colore"
            var codiceIngresso = username + nome
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
                val newCalendar = Calendar(nome, color, codiceIngresso)
                userDao.insertCalendar(newCalendar)
                val idCalendar = userDao.getIdFromCalendar(nome,codiceIngresso)
                val newAssoc = User_Calendar_id(username, idCalendar,"1")
                val idAssoc = userDao.selectUserCalendar(username,idCalendar)
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
/*
        calendario_esistente.setOnClickListener{
            val edit = findViewById<EditText>(com.example.calendario.R.id.editText_esistente)
            val codice_calendario = edit.text.toString()
            val intent = Intent(this,Home::class.java)


            if(codice_calendario.isNotEmpty()) {

                if (userDao.selectIdbyCodice(codice_calendario)!=null) {
                    val newAssoc = User_Calendar_id(username, userDao.selectIdbyCodice(codice_calendario),"0")
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

 */

        calendario_esistente.setOnClickListener {
            val edit = findViewById<EditText>(com.example.calendario.R.id.editText_esistente)
            val codice_calendario = edit.text.toString()
            val intent = Intent(this, CalendarAdded::class.java)

            if (codice_calendario.isNotEmpty()) {
                if (userDao.selectIdbyCodice(codice_calendario) != null) {
                    // Qui utilizziamo la callback per ottenere l'ID da Firebase
                    selectIdbyCodiceInFireBase(codice_calendario) { idFromFirebase ->
                        // Creiamo il nuovo oggetto User_Calendar_id con l'ID ottenuto da Firebase
                        val newAssoc = User_Calendar_id(username, idFromFirebase, "0")

                        // Inseriamo il nuovo oggetto nel database locale
                        userDao.insertUserCalendarId(newAssoc)

                        intent.putExtra("Username", username)

                        // Inseriamo anche l'oggetto nel database Firebase
                        val ref = database.getReference("assocs")
                        val newAssocRef = ref.push()
                        newAssocRef.setValue(newAssoc)

                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Il codice del calendario non è corretto!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Inserisci il codice del calendario!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

private fun selectIdbyCodiceInFireBase(codice_calendario: String, callback: (Long) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.reference.child("calendars")

    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (calendarSnapshot in snapshot.children) {
                val calendarData = calendarSnapshot.getValue(Calendar::class.java)

                calendarData?.let { calendar ->
                    if (calendar.codiceIngresso.toString() == codice_calendario) {
                        callback(calendar.id)
                        return
                    }
                }
            }
            callback(0) // Ritorna 0 se nessun match è stato trovato
        }

        override fun onCancelled(error: DatabaseError) {
            // Gestisci eventuali errori
            println("Errore nel recupero dei dati: ${error.message}")
            callback(0) // Gestione dell'errore, ritorna 0
        }
    })
}
