package com.example.planit

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import roomData.UserDatabase



class Event: AppCompatActivity()  {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("events")

        val userDao = UserDatabase.getInstance(application).dao()
        val selectedDate = intent.getStringExtra("data_selezionata")
        val id = intent.getLongExtra("id_calendario",0)
        val username = intent.getStringExtra("username")
        val nomeCal = intent.getStringExtra("nome_cal")
        val data = findViewById<TextView>(R.id.Data)
        data.text = selectedDate

        val btnIndietro = findViewById<Button>(R.id.Indietro)
        val btnInizio = findViewById<Button>(R.id.Btn_inizio)
        val btnFine = findViewById<Button>(R.id.Btn_fine)
        val btnCrea = findViewById<Button>(R.id.Btn_crea)
        val oraInizio = findViewById<TextView>(R.id.ora_inizio)
        val oraFine = findViewById<TextView>(R.id.ora_fine)

        // Bottone per tornare alla pagina del giorno
        btnIndietro.setOnClickListener{
            val intent = Intent (this, Day::class.java)
            intent.putExtra("data_selezionata",selectedDate)
            intent.putExtra("id_calendario",id)
            intent.putExtra("username",username)
            intent.putExtra("nome_cal",nomeCal)
            startActivity(intent)
        }

        // Apro la finestra per scegliere l'orario(inizio) e inserisco l'orario scelto nella textview sotto
        btnInizio.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                oraInizio.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        }

        // Apro la finestra per scegliere l'orario(fine) e inserisco l'orario scelto nella textview sotto
        btnFine.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                oraFine.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        }

        // Bottone per passare i dati dell'evento alla pagina del giorno scelto
        btnCrea.setOnClickListener {
            val titoloEt = findViewById<EditText>(R.id.editText_titolo)
            val titolo = titoloEt.text.toString()
            val titoloEdit = titoloEt.toString()
            val inizioTv = findViewById<TextView>(R.id.ora_inizio)
            val inizio = inizioTv.text.toString()
            val fineTv = findViewById<TextView>(R.id.ora_fine)
            val fine = fineTv.text.toString()
            val descrizioneEt = findViewById<EditText>(R.id.descrizione)
            val descrizione = descrizioneEt.text.toString()
            val data = selectedDate.toString()
            val intent = Intent(this, Day::class.java)
            if(titoloEdit.isNotEmpty() && inizio.isNotEmpty() && fine.isNotEmpty() && inizio <= fine){
                val newEvent = Event(titolo,data,inizio,fine,descrizione,id)
                userDao.insertEvent(newEvent)
                val ref = database.getReference("events")
                val newEventRef = ref.push()
                newEventRef.setValue(newEvent)
                intent.putExtra("data_selezionata",selectedDate)
                intent.putExtra("id_calendario",id)
                intent.putExtra("username",username)
                intent.putExtra("nome_cal",nomeCal)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Solo la descrizione può essere vuota!", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(this, "L'orario di inizio deve essre minore all'orario di fine!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}