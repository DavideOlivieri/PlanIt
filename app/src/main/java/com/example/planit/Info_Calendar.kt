package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import roomData.User
import roomData.UserDatabase

class Info_Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_calendario)

        // inizializzazione variabili
        val id = intent.getLongExtra("id_calendario", 0)
        val titoloView = findViewById<TextView>(R.id.titolo)
        val codiceView = findViewById<TextView>(R.id.codicepartecipazione)


        val userDao = UserDatabase.getInstance(application).dao()

        val currentCalendar = userDao.selectCalendarbyId(id)

        titoloView.setText(currentCalendar.titolo)
        codiceView.setText(currentCalendar.codiceIngresso)



        val users = userDao.selectAllUserbyId(id)

        for(i in users.indices){
            val button = addButton(users[i])
            button.setTag(users[i])
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