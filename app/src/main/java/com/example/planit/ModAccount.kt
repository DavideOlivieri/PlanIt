package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.UserDatabase

// Activity necessaria per far modificare la password e l'email dell'account
class ModAccount : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("users")

        // inizializzazione variabili
        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.password)
        val cnfpass = findViewById<EditText>(R.id.confermapassword)
        val btnConferma = findViewById<Button>(R.id.confermabtn)
        val username = intent.getStringExtra("Username")

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Calendario::class.java)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        val userDao = UserDatabase.getInstance(application).dao()

        // controllo dei valori inseriti
        btnConferma.setOnClickListener {
            val strPass: String = pass.text.toString()
            val strcnfPass: String = cnfpass.text.toString()
            val stremail: String = email.text.toString()
            if (stremail.isNotEmpty()) {
                if (strPass.isNotEmpty()) {
                    if (strcnfPass.isNotEmpty()) {
                        if (strPass == strcnfPass) {
                            if (strPass.contains('1') || strPass.contains('2') || strPass.contains('3') || strPass.contains('4') || strPass.contains('5') || strPass.contains('6') || strPass.contains('7') || strPass.contains('8') || strPass.contains('9') || strPass.contains('0')) {
                                if (stremail.contains('@') && (stremail.contains(".com") || stremail.contains(".it"))) {
                                    if (username != null) {
                                        userDao.modUser(username, strPass, stremail)
                                        myRef.child(username).child("email").setValue(stremail)
                                        myRef.child(username).child("password").setValue(strPass)
                                        Toast.makeText(this, "Modifica effettuata!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    val intentDone = Intent(this, Home::class.java)
                                    intentDone.putExtra("Username", username)
                                    startActivity(intentDone)
                                } else {
                                    Toast.makeText(this, "Inserisci un indirizzo email valido!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }else{
                                Toast.makeText(this, "Inserisci una Password adeguata (deve contenere almeno un numero)", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(this, "La password confermata non è corretta!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else{
                        Toast.makeText(this, "Confermara la password!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(this, "Inserisci una Password!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this, "Inserisci un indirizzo email!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}