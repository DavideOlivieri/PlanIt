package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import roomData.Calendar
import roomData.Event
import roomData.UserDatabase
import roomData.User_Calendar_id

class CalendarAdded : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendaradded)

        val tapScreen = findViewById<RelativeLayout>(R.id.activity_tap)
        val username = intent.getStringExtra("Username")

        tapScreen.setOnClickListener{
            val intent =  Intent(this,Home::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
        }

        val userDao = UserDatabase.getInstance(application).dao()


        if (username != null) {
            getAssocDataFromFirebase(username)
        }
        val allAssocs = userDao.selectAllUserCalendar()
        for(assoc in allAssocs) {
            getCalendarsByCalendarIdFromFirebase(assoc.calendar_id.toString())
            getEventsByCalendarIdFromFirebase(assoc.calendar_id.toString())
        }
    }

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

                val calendarsToDelete = userDao.selectCalendarsNotInList(calendarId, calendarList.map { it.id })
                userDao.deleteCalendars(calendarsToDelete)

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
    ////////////////

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

                val eventsToDelete = userDao.selectEventsNotInList(calendarId, eventList.map { it.id })
                userDao.deleteEvents(eventsToDelete)

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
    ////////////

    private fun getAssocDataFromFirebase(username: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("assocs")

        myRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val assocList = mutableListOf<User_Calendar_id>()

                for (assocSnapshot in snapshot.children) {
                    val assocData = assocSnapshot.getValue(User_Calendar_id::class.java)
                    assocData?.let {
                        assocList.add(assocData)
                    }
                }

                val userDao = UserDatabase.getInstance(application).dao()

                val associationsToDelete = userDao.selectAssociationsNotInList(username, assocList.map { it.id })
                userDao.deleteUserCalendarIds(associationsToDelete)

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
}