package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.calendario.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.Calendar
import roomData.Event
// import com.example.calendario.databinding.ActivityHomeBinding
import roomData.UserDatabase
import roomData.User_Calendar_id
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Home : AppCompatActivity() {

    //lateinit var binding: ActivityHomeBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val database = Firebase.database
        val myRef = database.getReference("calendars")

        //inizializzazione variabili
        val account = findViewById<ImageView>(R.id.Account)
        val btnAdd = findViewById<Button>(R.id.Aggiungi_Calendario)
        val username = intent.getStringExtra("Username")

        val userDao = UserDatabase.getInstance(application).dao()


        // reperimento dei dati aggiornati da firebase
        if (username != null) {
            getAssocDataFromFirebase(username)
        }
        val allAssocs = userDao.selectAllUserCalendar()
        for(assoc in allAssocs) {
            getCalendarsByCalendarIdFromFirebase(assoc.calendar_id.toString())
            getEventsByCalendarIdFromFirebase(assoc.calendar_id.toString())
        }



        // passaggio alla schermata di ModAccount
        account.setOnClickListener {
            val intent = Intent(this, Account::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        // Aggiungi un nuovo Calendario (schermata scelta dati calendario)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddCalendar::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        // crea un OnClickListener comune per tutti i bottoni
        val viewCal = View.OnClickListener { view ->
            val titolo = view.getTag() as String
            val id = userDao.getIdFromTitoloandUser(titolo, username)
            // crea un Intent per l'Activity che vuoi aprire
            val intent = Intent(this, Calendario::class.java)

            intent.putExtra("username", username)
            intent.putExtra("id_calendario", id)
            // avvia l'Activity
            startActivity(intent)
        }

        // funzione per eliminare un calendario
        var builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        val longClick = View.OnLongClickListener { view ->
            builder.setTitle("Attenzione!")
                .setMessage("Sei sicuro di voler eliminare questo calendario?")
                .setCancelable(true)
                .setPositiveButton("Si") { dialogInterface, it ->
                    val titolo = view.getTag() as String
                    val long = userDao.getIdFromTitoloandUser(titolo, username)
                    if (userDao.selectUserCalendar(username, long).livello == "1") {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.reference.child("calendars")
                        val query = myRef.orderByChild("id").equalTo(long.toDouble())
                        deleteCalendarById(query)
                        userDao.deleteCalendar(userDao.selectCalendarbyId(long))





                        deleteAssocById(long.toDouble(), username)

                        userDao.deleteUserFromCalendar(userDao.selectUserCalendar(username,long))


                    } else {
                        deleteAssocById(long.toDouble(), username)

                        userDao.deleteUserFromCalendar(userDao.selectUserCalendar(username,long))
                    }
                    Toast.makeText(
                        this,
                        "Hai eliminato il calendario: " + titolo,
                        Toast.LENGTH_SHORT
                    ).show()
                    recreate()
                }
                .setNegativeButton("No") { dialogInterface, it -> dialogInterface.cancel() }
                .show()
            true
        }


        val calendars = userDao.selectAllIdCalendarofUser(username)

        // ciclo per visualizzare tutti i calendari dell'utente loggato
        for (i in calendars.indices) {
            val button = addButton(calendars[i].titolo)
            button.setTag(calendars[i].titolo)
            button.setOnClickListener(viewCal)
            button.setOnLongClickListener(longClick)
        }


        // bottone per attività del giorno
        val btnOpentodayevent = findViewById<Button>(R.id.btnOpentodayevent)
        btnOpentodayevent.setOnClickListener {
            if (username != null) {
                replaceFragment(todayeventFragment(), username)
            }
        }
    }

    val currentDate = Date() // Ottieni la data corrente
    val dateFormat = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.ITALY
    ) // Crea un oggetto SimpleDateFormat con il formato desiderato
    val formattedDate = dateFormat.format(currentDate) // Formatta la data nel formato specificato

    private fun replaceFragment(fragment: Fragment, username: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framefragment, fragment)
        val bundle = Bundle()
        bundle.putString("Data", formattedDate)
        fragment.arguments = bundle
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        val frag = findViewById<FrameLayout>(R.id.framefragment)
        frag.visibility = View.VISIBLE
    }


    // funzione per aggiungere alla schermata di home il bottone del calendario
    fun addButton(nome: String): Button {
        val linear = findViewById<LinearLayout>(R.id.linearlayout)
        val inflater = LayoutInflater.from(this)
        val buttonLayout = inflater.inflate(R.layout.calendar_button, null)
        val button = buttonLayout.findViewById<Button>(R.id.button)
        button.setText(nome)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

       // Imposta i margini tra i bottoni
        layoutParams.setMargins(0, 30, 0, 0)

       // Imposta i parametri del layout
        buttonLayout.layoutParams = layoutParams

        button.id = View.generateViewId()
        var button_id = button.id
        linear.addView(buttonLayout, layoutParams)
        return button
    }


    //nuova

    private fun getCalendarsByCalendarIdFromFirebase(calendarId: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("calendars")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val calendarList = mutableListOf<Calendar>()

                for (calendarSnapshot in snapshot.children) {
                    val calendarData = calendarSnapshot.getValue(Calendar::class.java)

                    calendarData?.let { calendar ->
                        if (calendar.id.toString() == calendarId) {
                            calendarList.add(calendar)
                        }
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (calendar in calendarList) {
                    if (userDao.selectCalendarbyId(calendar.id) == null) {
                        val newCalendar = Calendar(calendar.titolo, null, calendar.codiceIngresso)
                        newCalendar.id = calendar.id
                        userDao.insertCalendar(newCalendar)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
                println("Errore nel recupero dei dati: ${error.message}")
            }
        })
    }

    //new
    private fun getEventsByCalendarIdFromFirebase(calendarId: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("events")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()

                for (eventSnapshot in snapshot.children) {
                    val eventData = eventSnapshot.getValue(Event::class.java)

                    eventData?.let { event ->
                        if (event.calendar_id.toString() == calendarId) {
                            eventList.add(event)
                        }
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (event in eventList) {
                    if (userDao.selectEvent(event.id) == null) {
                        val newEvent = Event(
                            event.titolo,
                            event.data,
                            event.orario_inizio,
                            event.orario_fine,
                            event.descrizione,
                            event.calendar_id
                        )
                        newEvent.id = event.id
                        userDao.insertEvent(newEvent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
                println("Errore nel recupero dei dati: ${error.message}")
            }
        })
    }

    //new
    private fun getAssocDataFromFirebase(username: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("assocs")

        myRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val assocList = mutableListOf<User_Calendar_id>()

                for (assocSnapshot in snapshot.children) {
                    val assocData = assocSnapshot.getValue(User_Calendar_id::class.java)
                    assocData?.let {
                        assocList.add(assocData)
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (assoc in assocList) {
                    if (userDao.selectUserCalendarbyID(assoc.id) == null) {
                        val newAssoc = User_Calendar_id(assoc.username, assoc.calendar_id, assoc.livello)
                        newAssoc.id = assoc.id
                        userDao.insertUserCalendarId(newAssoc)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
                println("Errore nel recupero dei dati: ${error.message}")
            }
        })
    }

    private fun deleteCalendarById(query: Query) {


        // Eseguire la query per trovare il calendario con il titolo specificato

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (calendarSnapshot in snapshot.children) {
                    calendarSnapshot.ref.removeValue()


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


    private fun deleteAssocById(long: Double, username: String?) {


        val database1 = FirebaseDatabase.getInstance()
        val myRef1 = database1.reference.child("assocs")
        // Eseguire la query per trovare il calendario con il titolo specificato
        val query = myRef1.orderByChild("calendar_id").equalTo(long)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (assocSnapshot in snapshot.children) {
                    val user = assocSnapshot.child("username").getValue(String::class.java)
                    if (user == username) {
                        assocSnapshot.ref.removeValue()
                            .addOnSuccessListener {
                                println("Evento eliminato con successo.")
                            }
                            .addOnFailureListener { exception ->
                                println("Errore nell'eliminazione dell'evento: ${exception.message}")
                            }
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






