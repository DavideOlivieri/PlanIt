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
        val nome_cal = intent.getStringExtra("nome_cal")
        val data = findViewById<TextView>(R.id.Data)
        data.text = selectedDate

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        val Btn_inizio = findViewById<Button>(R.id.Btn_inizio)
        val Btn_fine = findViewById<Button>(R.id.Btn_fine)
        val Btn_crea = findViewById<Button>(R.id.Btn_crea)
        val ora_inizio = findViewById<TextView>(R.id.ora_inizio)
        val ora_fine = findViewById<TextView>(R.id.ora_fine)

        // Bottone per tornare alla pagina del giorno
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Day::class.java)
            intent.putExtra("data_selezionata",selectedDate)
            intent.putExtra("id_calendario",id)
            intent.putExtra("username",username)
            intent.putExtra("nome_cal",nome_cal)
            startActivity(intent)
        }

        // Apro la finestra per scegliere l'orario(inizio) e inserisco l'orario scelto nella textview sotto
        Btn_inizio.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                ora_inizio.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        }

        // Apro la finestra per scegliere l'orario(fine) e inserisco l'orario scelto nella textview sotto
        Btn_fine.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                ora_fine.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        }

        // Bottone per passare i dati dell'evento alla pagina del giorno scelto
        Btn_crea.setOnClickListener {
            val titolo_et = findViewById<EditText>(R.id.editText_titolo)
            val titolo = titolo_et.text.toString()
            val titolo_edit = titolo_et.toString()
            val inizio_tv = findViewById<TextView>(R.id.ora_inizio)
            val inizio = inizio_tv.text.toString()
            val fine_tv = findViewById<TextView>(R.id.ora_fine)
            val fine = fine_tv.text.toString()
            val descrizione_et = findViewById<EditText>(R.id.descrizione)
            val descrizione = descrizione_et.text.toString()
            val data = selectedDate.toString()
            val intent = Intent(this, Day::class.java)
            if(titolo_edit.isNotEmpty() && inizio.isNotEmpty() && fine.isNotEmpty() && inizio <= fine){
                val newEvent = Event(titolo,data,inizio,fine,descrizione,id)
                userDao.insertEvent(newEvent)
                myRef.child(newEvent.id.toString()).child("titolo_evento").setValue(titolo)
                myRef.child(newEvent.id.toString()).child("data").setValue(data)
                myRef.child(newEvent.id.toString()).child("orario_inizio").setValue(inizio)
                myRef.child(newEvent.id.toString()).child("orario_fine").setValue(fine)
                myRef.child(newEvent.id.toString()).child("descrizione").setValue(descrizione)
                myRef.child(newEvent.id.toString()).child("calendar_id").setValue(id)
                intent.putExtra("data_selezionata",selectedDate)
                intent.putExtra("id_calendario",id)
                intent.putExtra("username",username)
                intent.putExtra("nome_cal",nome_cal)
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