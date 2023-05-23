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
import roomData.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import roomData.UserDatabase



class Event: AppCompatActivity()  {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        val userDao = UserDatabase.getInstance(application).dao()
        val selectedDate = intent.getStringExtra("Date")
        val id = intent.getLongExtra("id_calendario", 0)
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
            val fine = fine_tv.toString()
            val descrizione_et = findViewById<EditText>(R.id.descrizione)
            val descrizione = descrizione_et.toString()
            val data = selectedDate.toString()
            val intent = Intent(this, Day::class.java)
            if(titolo_edit.isNotEmpty()){
                val newEvent = Event(titolo,data,inizio,fine,descrizione,id)
                userDao.insertEvent(newEvent)

                /*
                intent.putExtra("titolo",titolo_edit)
                intent.putExtra("inizio",inizio_ev)
                intent.putExtra("fine",fine_ev)
                intent.putExtra("id_calendario",id)
                 */
                startActivity(intent)
            } else{
                Toast.makeText(this, "Solo la descrizione può essere vuota!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}