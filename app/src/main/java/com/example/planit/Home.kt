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


var titolo_calendario: String? = null
var codice_ingresso: String? = null
var id_calendario: Long? = null

var usernameAssoc: String? = null
var id_calendarAssoc: Long? = null
var livello: Int? = null
var idAssoc: Long? = null

var titolo_evento: String? = null
var data: String? = null
var orario_inizio: String? = null
var orario_fine: String? = null
var descrizione: String? = null
var calendar_id: Long? = null

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

        /*
        if (username != null) {
            getAllCalendarsDataFromFirebase(username)
        }
        if (username != null) {
            getAllEventsDataFromFirebase(username)
        }*/

        getAllCalendarsDataFromFirebase()
        //getAllEventsDataFromFirebase()
        //getAllAssocsDataFromFirebase()




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
                        userDao.deleteCalendar(userDao.selectCalendarbyId(long))
                        myRef.child(userDao.getIdFromTitoloandUser(titolo, username).toString())
                            .removeValue()
                    } else {
                        userDao.deleteUserFromCalendar(userDao.selectUserCalendar(username, long))
                        if (username != null) {
                            myRef.child(userDao.getIdFromTitoloandUser(titolo, username).toString())
                                .child("Partecipanti").child(username).removeValue()
                        }
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

        /*val isOk = intent.getBooleanExtra("isOk", false)

        // Aggiungi nuovi calendari
        //se le informazioni riguardo al calendario soddisfano i requisiti allora aggiungo il bottone
        if (isOk) {
            val button = addButton()

            val button1 = findViewById<Button>(button)
            button1.setOnClickListener {
                val intent = Intent(this, Calendario::class.java)
                intent.putExtra("Username", username)
                startActivity(intent)

            }

        }
        */

        val calendars = userDao.selectAllIdCalendarofUser(username)

        for (i in calendars.indices) {
            val button = addButton(calendars[i].titolo)
            button.setTag(calendars[i].titolo)
            button.setOnClickListener(viewCal)
            button.setOnLongClickListener(longClick)
        }

        /*
            binding = ActivityHomeBinding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.btnOpentodayevent.setOnClickListener {
                    replaceFragment(todayeventFragment())
                }

         */

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


    /*
//Funzione per aggiungere alla schermata di home il bottone del calendario
fun addButton(): Int {
    val linear = findViewById<LinearLayout>(R.id.linearlayout)
    val inflater = LayoutInflater.from(this)
    val nome = intent.getStringExtra("Nome calendario")
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
//     var editText: EditText
//     editText = EditText(this)
//     editText.setText(button_id.toString())

    linear.addView(buttonLayout,layoutParams)
//     linear.addView(editText,layoutParams)
    return button_id
}

*/


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
//     var editText: EditText
//     editText = EditText(this)
//     editText.setText(button_id.toString())

        linear.addView(buttonLayout, layoutParams)
//     linear.addView(editText,layoutParams)
        return button
    }

    /*
@SuppressLint("ResourceAsColor")
fun addButton(){
var button: Button
val nome = intent.getStringExtra("Nome calendario")
val relative = findViewById<RelativeLayout>(R.id.linearlayout)
button = Button(this)
button.setTextColor(R.color.black)
button.setText(nome)
button.setBackgroundColor(R.color.white)  // non funziona colore

val params = RelativeLayout.LayoutParams(
    RelativeLayout.LayoutParams.MATCH_PARENT,
    RelativeLayout.LayoutParams.WRAP_CONTENT,
)
params.addRule(RelativeLayout.BELOW)
relative.addView(button,params)
}
*/

    /*
    private fun getAllCalendarsDataFromFirebase(username: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("calendars")

        myRef.orderByChild(username).equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val calendarList = mutableListOf<Calendar>()

                for (calendarSnapshot in snapshot.children) {
                    val calendarData = calendarSnapshot.getValue(Calendar::class.java)
                    calendarData?.let {
                        calendarList.add(calendarData)
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (calendar in calendarList) {
                    if (userDao.selectCalendarbyId(calendar.id) == null) {

                        var titolo_calendario = calendar.titolo
                        var codice_ingresso = calendar.codiceIngresso
                        var id_calendario = calendar.id

                        var newCalendar = Calendar(titolo_calendario, null ,codice_ingresso)
                        newCalendar.id=id_calendario
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


    private fun getAllAssocsDataFromFirebase(username: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("assocs")

        myRef.orderByChild(username).equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                        if (userDao.selectCalendarbyId(assoc.id) == null) {

                            var titolo_calendario = calendar.titolo
                            var codice_ingresso = calendar.codiceIngresso
                            var id_calendario = calendar.id

                            var newCalendar = Calendar(titolo_calendario, null ,codice_ingresso)
                            newCalendar.id=id_calendario
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


    private fun getAllEventsDataFromFirebase(username: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("events")

        myRef.orderByChild(username).equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val eventList = mutableListOf<Event>()

                    for (eventSnapshot in snapshot.children) {
                        val eventData = eventSnapshot.getValue(Event::class.java)
                        eventData?.let {
                            eventList.add(eventData)
                        }
                    }

                    val userDao = UserDatabase.getInstance(application).dao()

                    for (event in eventList) {
                        if (userDao.selectEvent(event.id) == null) {

                            var titolo_evento = event.titolo
                            var data = event.data
                            var orario_inizio = event.orario_inizio
                            var orario_fine =  event.orario_fine
                            var descrizione = event.descrizione
                            var calendar_id = event.calendar_id

                            var newEvent = Event(titolo_evento, data,orario_inizio, orario_fine, descrizione, calendar_id)
                            newEvent.id=event.id
                            userDao.insertEvent(newEvent)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Gestisci eventuali errori
                    println("Errore nel recupero dei dati: ${error.message}")
                }
            })
    }*/
    private fun getAllCalendarsDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("calendars")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val calendarList = mutableListOf<Calendar>()

                for (calendarSnapshot in snapshot.children) {
                    val calendarData = calendarSnapshot.getValue(Calendar::class.java)
                    calendarData?.let {
                        calendarList.add(calendarData)
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (calendar in calendarList) {
                    if (userDao.selectCalendarbyCodice(calendar.codiceIngresso) == null) {
                        var titolo_calendario = calendar.titolo
                        var codice_ingresso = calendar.codiceIngresso

                        var newCalendar = Calendar(titolo_calendario, null ,codice_ingresso)
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
/*
    private fun getAllEventsDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("events")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()

                for (eventSnapshot in snapshot.children) {
                    val eventData = eventSnapshot.getValue(Event::class.java)
                    eventData?.let {
                        eventList.add(eventData)
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                for (event in eventList) {
                    if (userDao.selectEvent(event.id) == null) {

                        var titolo_evento = event.titolo
                        var data = event.data
                        var orario_inizio = event.orario_inizio
                        var orario_fine = event.orario_fine
                        var descrizione = event.descrizione
                        var calendar_id = event.calendar_id
                        var id_evento = event.id

                        var newEvent = Event(
                            titolo_evento,
                            data,
                            orario_inizio,
                            orario_fine,
                            descrizione,
                            calendar_id
                        )
                        newEvent.id = id_evento
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

    private fun getAllAssocsDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("assocs")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

                        var usernameAssoc = assoc.username
                        var id_calendarAssoc = assoc.calendar_id
                        var livello = assoc.livello
                        var id_assoc = assoc.id

                        var newAssoc = User_Calendar_id(usernameAssoc, id_calendarAssoc, livello)

                        newAssoc.id = id_assoc
                        userDao.insertUserCalendarId(newAssoc)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
                println("Errore nel recupero dei dati: ${error.message}")
            }
        })
    }*/
}






