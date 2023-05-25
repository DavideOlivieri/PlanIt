package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.calendario.R
import roomData.User
import roomData.UserDatabase


class Info_Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_calendario)

        // inizializzazione variabili
        val username = intent.getStringExtra("username")
        val id = intent.getLongExtra("id_calendario", 0)
        val titoloView = findViewById<TextView>(R.id.titolo)
        val codiceView = findViewById<TextView>(R.id.codicepartecipazione)

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Calendario::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        val userDao = UserDatabase.getInstance(application).dao()

        val currentCalendar = userDao.selectCalendarbyId(id)

        titoloView.setText(currentCalendar.titolo)
        codiceView.setText(currentCalendar.codiceIngresso)

/*
        if(userDao.selectLivello(username,id)==1){
            val informazioni = findViewById<TextView>(R.id.informazioni)
           informazioni.setVisibility(View.VISIBLE)
            viewUser = View.OnClickListener {view->
                val user = view.getTag() as String
                userDao.deleteUserFromCalendar(userDao.selectUserCalendar(user,id))
                Toast.makeText(this, "Hai eliminato dal calendario: "+user, Toast.LENGTH_SHORT).show()
            }
        }

 */
       // controlla il livello dell'utente e se è uguale ad 1 mostra il testo "Tieni premuto un partecipante per eliminare"
        if(userDao.selectLivello(username,id)==1) {
            val informazioni = findViewById<TextView>(R.id.informazioni)
            informazioni.setVisibility(View.VISIBLE)
        }

        var builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)

        val cancella = View.OnLongClickListener{view ->
            builder.setTitle("Attenzione!")
                .setMessage("Sei sicuro di voler eliminare questo utente dal calendario?")
                .setCancelable(true)
                .setPositiveButton("Si"){dialogInterface,it ->  val user = view.getTag() as String
                    userDao.deleteUserFromCalendar(userDao.selectUserCalendar(user,id))
                    Toast.makeText(this, "Hai eliminato l'utente: " + user, Toast.LENGTH_SHORT).show()
                    recreate()}
                .setNegativeButton("No"){dialogInterface,it ->dialogInterface.cancel()}
                .show()
            true
        }

        val users = userDao.selectAllUserbyId(id)

        for(i in users.indices){
            val button = addButton(users[i])
            button.setTag(users[i])
            button.setOnLongClickListener(cancella)
        }
    }

    fun addButton(nome: String): Button{
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

        linear.addView(buttonLayout,layoutParams)
        //     linear.addView(editText,layoutParams)
        return button
    }
}