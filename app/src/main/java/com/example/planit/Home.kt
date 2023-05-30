package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.calendario.R
import roomData.UserDatabase


class Home : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //inizializzazione variabili
        val account = findViewById<ImageView>(R.id.Account)
        val btnAdd = findViewById<Button>(R.id.Aggiungi_Calendario)
        val username = intent.getStringExtra("Username")

        val userDao = UserDatabase.getInstance(application).dao()


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
        val viewCal = View.OnClickListener {view->
            val titolo = view.getTag() as String
            val id = userDao.getIdFromTitoloandUser(titolo,username)
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
                .setMessage("Sei sicuro di voler eliminare questo evento?")
                .setCancelable(true)
                .setPositiveButton("Si"){dialogInterface,it -> val titolo = view.getTag() as String
                    val long = userDao.getIdFromTitoloandUser(titolo,username)
                    userDao.deleteCalendar(userDao.selectCalendarbyId(long))
                    Toast.makeText(this, "Hai eliminato l'evento: " + titolo, Toast.LENGTH_SHORT).show()
                    recreate()}
                .setNegativeButton("No"){dialogInterface,it ->dialogInterface.cancel()}
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

        for(i in calendars.indices){
            val button = addButton(calendars[i].titolo)
            button.setTag(calendars[i].titolo)
            button.setOnClickListener(viewCal)
            button.setOnLongClickListener(longClick)
        }


        val button: Button = findViewById(R.id.btnOpentodayevent)
        button.setOnClickListener {
            val fragment = todayeventFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_todayevent, fragment)
                .commit()
            //fragmentContainerView.visibility = View.VISIBLE
        }



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


    fun addButton(nome: String): Button{
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

        linear.addView(buttonLayout,layoutParams)
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
    }





