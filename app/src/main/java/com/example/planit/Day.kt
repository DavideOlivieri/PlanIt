package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.calendario.R
import roomData.UserDatabase

class Day: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)


        val selectedDate = intent.getStringExtra("data_selezionata")
        val id = intent.getLongExtra("id_calendario", 0)
        val Btn_agg = findViewById<Button>(R.id.Aggiungi_Calendario)
        val data = findViewById<TextView>(R.id.Data)
        val calendario = findViewById<TextView>(R.id.Nome_calendario)
        data.setText(selectedDate)
        val userDao = UserDatabase.getInstance(application).dao()


        Btn_agg.setOnClickListener {
            val intent = Intent(this, Event::class.java)
            intent.putExtra("Date", selectedDate)
            intent.putExtra("id_calendario", id)
            startActivity(intent)
        }

        val events = userDao.getEventsByCalendarId(id)

        for (i in events.indices) {
            addCard(events[i].titolo,events[i].orario_inizio)
        }

    }
     private fun addCard(titolo: String,inizio: String) {
         val linear = findViewById<LinearLayout>(R.id.linearlayout)
         val inflater = LayoutInflater.from(this)
         val card_layout = inflater.inflate(R.layout.event_view, null)
         val card = card_layout.findViewById<CardView>(R.id.card)
         val parentOfChild: ViewGroup? = card.parent as? ViewGroup
         parentOfChild?.removeView(card)

         val titolo_card = card.findViewById<TextView>(R.id.titolo)
         titolo_card.setText(titolo)
         val inizio_card = card.findViewById<TextView>(R.id.orario_i)
         inizio_card.setText(inizio)


         linear.addView(card)
     }
}