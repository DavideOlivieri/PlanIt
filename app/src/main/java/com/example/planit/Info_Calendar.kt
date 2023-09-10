package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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


class Info_Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_calendario)

        val database = Firebase.database
        val myRef = database.getReference("calendars")

        // inizializzazione variabili
        val username = intent.getStringExtra("username")
        val id = intent.getLongExtra("id_calendario", 0)
        val titoloView = findViewById<TextView>(R.id.titolo)
        val codiceView = findViewById<TextView>(R.id.codicepartecipazione)



        // bottone indietro
        val btnIndietro = findViewById<Button>(R.id.Indietro)
        btnIndietro.setOnClickListener {
            val intent = Intent(this, Calendario::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // bottone per la condivisione del codice
        val shareButton: Button = findViewById(R.id.condivisione)
        shareButton.setOnClickListener {
            shareButtonClicked()
        }

        val userDao = UserDatabase.getInstance(application).dao()

        val currentCalendar = userDao.selectCalendarbyId(id)

        titoloView.setText(currentCalendar.titolo)
        codiceView.setText(currentCalendar.codiceIngresso)


        // controlla il livello dell'utente e se è uguale ad 1 mostra il testo "Tieni premuto un partecipante per eliminare"
        if (userDao.selectLivello(username, id) == 1) {
            val informazioni = findViewById<TextView>(R.id.informazioni)
            informazioni.setVisibility(View.VISIBLE)
        }

        var builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)

        // eliminazione del partecipante
        val cancella = View.OnLongClickListener { view ->
            if (userDao.selectUserCalendar(username, id).livello == "1") {
                builder.setTitle("Attenzione!")
                    .setMessage("Sei sicuro di voler eliminare questo utente dal calendario?")
                    .setCancelable(true)
                    .setPositiveButton("Si") { dialogInterface, it ->
                        val user = view.getTag() as String
                        if (user == username) {
                            Toast.makeText(
                                this,
                                "Non puoi eliminarti da solo :(",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            userDao.deleteUserFromCalendar(userDao.selectUserCalendar(user, id))
                            deleteAssocById(id.toDouble(), user)

                            Toast.makeText(
                                this,
                                "Hai eliminato l'utente: " + user,
                                Toast.LENGTH_SHORT
                            ).show()
                            recreate()
                        }
                    }
                    .setNegativeButton("No") { dialogInterface, it -> dialogInterface.cancel() }
                    .show()
            }
            true
        }





        val users = userDao.selectAllUserbyId(id)

        for (i in users.indices) {
            val button = addButton(users[i])
            button.setTag(users[i])
            button.setOnLongClickListener(cancella)
        }
    }

    // Condivisione del codice di partecipazione
    private fun shareButtonClicked() {
        val id = intent.getLongExtra("id_calendario", 0)

        val userDao = UserDatabase.getInstance(application).dao()

        val currentCalendar = userDao.selectCalendarbyId(id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, currentCalendar.codiceIngresso)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(intent, "Condividi con")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            // Nessuna app disponibile per la condivisione
            // Gestisci di conseguenza
        }
    }

    fun addButton(nome: String): Button {
        val linear = findViewById<LinearLayout>(R.id.linearlayout)
        val inflater = LayoutInflater.from(this)
        /*da cambiare*/
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
        //     var editText: EditText
        //     editText = EditText(this)
        //     editText.setText(button_id.toString())

        linear.addView(buttonLayout, layoutParams)
        //     linear.addView(editText,layoutParams)
        return button
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

