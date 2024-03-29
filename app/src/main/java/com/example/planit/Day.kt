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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.UserDatabase


class Day: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        // collegamento a firebase e inizializzazione variabili
        val database = Firebase.database
        val myRef = database.getReference("events")

        val selectedDate = intent.getStringExtra("data_selezionata")
        val id = intent.getLongExtra("id_calendario", 0)
        val nomeCal = intent.getStringExtra("nome_cal")
        val username = intent.getStringExtra("username")
        val btnAgg = findViewById<Button>(R.id.Aggiungi_Calendario)
        val data = findViewById<TextView>(R.id.Data)
        val calendario = findViewById<TextView>(R.id.Nome_calendario)
        calendario.setText(nomeCal)
        data.setText(selectedDate)
        val userDao = UserDatabase.getInstance(application).dao()


        // bottone indietro
        val btnIndietro = findViewById<Button>(R.id.Indietro)
        btnIndietro.setOnClickListener {
            val intent = Intent(this, Calendario::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // bottone aggiungi calendario
        btnAgg.setOnClickListener {
            val intent = Intent(this, Event::class.java)
            intent.putExtra("data_selezionata", selectedDate)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username", username)
            intent.putExtra("nome_cal", nomeCal)
            startActivity(intent)
        }


        var builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)

        // funzione per eliminare un evento
        val cancella = View.OnLongClickListener { view ->
            builder.setTitle("Attenzione!")
                .setMessage("Sei sicuro di voler eliminare questo evento?")
                .setCancelable(true)
                .setPositiveButton("Si") { dialogInterface, it ->
                    val titolo = view.getTag() as Int

                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.reference.child("events")
                    val query = myRef.orderByChild("id").equalTo(userDao.selectEvent(titolo).id.toDouble())
                    userDao.deleteEvent(userDao.selectEvent(titolo))
                    deleteEventById(query)
                    Toast.makeText(this, "Hai eliminato l'evento: " + titolo, Toast.LENGTH_SHORT)
                        .show()
                    recreate()
                }
                .setNegativeButton("No") { dialogInterface, it -> dialogInterface.cancel() }
                .show()
            true
        }


        val events = userDao.getEventsByCalendarId(id, selectedDate)

        // ciclo per visualizzare a schermo tutti gli eventi del giorno
        for (i in events.indices) {
            val card = addCard(
                events[i].titolo,
                events[i].orario_inizio,
                events[i].orario_fine,
                events[i].descrizione
            )
            card?.setTag(events[i].id)
            card?.setOnLongClickListener(cancella)
        }

    }

    private fun addCard(
        titolo: String,
        inizio: String,
        fine: String,
        descrizione: String
    ): CardView? {
        val linear = findViewById<LinearLayout>(R.id.linearlayout)
        val inflater = LayoutInflater.from(this)
        val cardLayout = inflater.inflate(R.layout.event_view, null)
        val card = cardLayout.findViewById<CardView>(R.id.card)
        val parentOfChild: ViewGroup? = card.parent as? ViewGroup
        parentOfChild?.removeView(card)

        val drawable = ContextCompat.getDrawable(this, R.drawable.background_event)
        card.background = drawable

        //inserisco all'interno selle textview i valori che prendo dal database per ogni evento
        val titoloCard = card.findViewById<TextView>(R.id.titolo)
        titoloCard.setText(titolo)
        val inizioCard = card.findViewById<TextView>(R.id.orario_i)
        inizioCard.setText(inizio)
        val fineCard = card.findViewById<TextView>(R.id.orario_f)
        fineCard.setText(fine)
        val descrizioneCard = card.findViewById<TextView>(R.id.descrizione)
        descrizioneCard.setText(descrizione)

        linear.addView(card)
        return card
    }


    private fun deleteEventById(query: Query) {


        // Eseguire la query per trovare il calendario con il titolo specificato

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (eventSnapshot in snapshot.children) {
                    eventSnapshot.ref.removeValue()


                            .addOnSuccessListener {
                                println("Evento eliminato con successo.")
                            }
                            .addOnFailureListener { exception ->
                                println("Errore nell'eliminazione dell'evento: ${exception.message}")
                            }
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestire eventuali errori
                println("Errore nella query: ${error.message}")
            }
        })
    }
    }




