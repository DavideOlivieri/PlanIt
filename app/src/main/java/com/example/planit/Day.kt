package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.calendario.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.UserDatabase


class Day: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        val database = Firebase.database
        val myRef = database.getReference("events")

        val selectedDate = intent.getStringExtra("data_selezionata")
        val id = intent.getLongExtra("id_calendario", 0)
        val nome_cal = intent.getStringExtra("nome_cal")
        val username = intent.getStringExtra("username")
        val Btn_agg = findViewById<Button>(R.id.Aggiungi_Calendario)
        val data = findViewById<TextView>(R.id.Data)
        val calendario = findViewById<TextView>(R.id.Nome_calendario)
        calendario.setText(nome_cal)
        data.setText(selectedDate)
        val userDao = UserDatabase.getInstance(application).dao()


        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Calendario::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        Btn_agg.setOnClickListener {
            val intent = Intent(this, Event::class.java)
            intent.putExtra("data_selezionata", selectedDate)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username",username)
            intent.putExtra("nome_cal",nome_cal)
            startActivity(intent)
        }


        var builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)

        val cancella = View.OnLongClickListener{view ->
            builder.setTitle("Attenzione!")
                .setMessage("Sei sicuro di voler eliminare questo evento?")
                .setCancelable(true)
                .setPositiveButton("Si"){dialogInterface,it -> val titolo = view.getTag() as Int
                    userDao.deleteEvent(userDao.selectEvent(titolo))
                    myRef.child(userDao.selectEvent(titolo).id.toString()).removeValue()
                    Toast.makeText(this, "Hai eliminato l'evento: " + titolo, Toast.LENGTH_SHORT).show()
                recreate()}
                .setNegativeButton("No"){dialogInterface,it ->dialogInterface.cancel()}
                .show()
            true
        }



        val events = userDao.getEventsByCalendarId(id,selectedDate)

        for (i in events.indices) {
            val card = addCard(events[i].titolo,events[i].orario_inizio,events[i].orario_fine,events[i].descrizione)
            card?.setTag(events[i].id)
            card?.setOnLongClickListener(cancella)
        }

    }
     private fun addCard(titolo: String,inizio: String,fine: String,descrizione: String): CardView? {
         val linear = findViewById<LinearLayout>(R.id.linearlayout)
         val inflater = LayoutInflater.from(this)
         val card_layout = inflater.inflate(R.layout.event_view, null)
         val card = card_layout.findViewById<CardView>(R.id.card)
         val parentOfChild: ViewGroup? = card.parent as? ViewGroup
         parentOfChild?.removeView(card)

         val drawable = ContextCompat.getDrawable(this, R.drawable.background_event)
         card.background = drawable

         //inserisco all'interno selle textview i valori che prendo dal database per ogni evento
         val titolo_card = card.findViewById<TextView>(R.id.titolo)
         titolo_card.setText(titolo)
         val inizio_card = card.findViewById<TextView>(R.id.orario_i)
         inizio_card.setText(inizio)
         val fine_card = card.findViewById<TextView>(R.id.orario_f)
         fine_card.setText(fine)
         val descrizione_card = card.findViewById<TextView>(R.id.descrizione)
         descrizione_card.setText(descrizione)

         linear.addView(card)
         return card
     }
}


