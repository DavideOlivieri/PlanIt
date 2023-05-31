package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.calendario.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import roomData.User
import roomData.UserDatabase

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // passaggio alla schermata di logIn
        val siAccount = findViewById<TextView>(R.id.login_view)

        siAccount.setOnClickListener{
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("users")



        // inizializzazione variabili
        val email = findViewById<EditText>(R.id.email)
        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)
        val cnfpass = findViewById<EditText>(R.id.confermapassword)
        val btnSignUp = findViewById<Button>(R.id.signupbtn)

        var newUser: User
        val userDao = UserDatabase.getInstance(application).dao()



        // controllo dei valori inseriti
        btnSignUp.setOnClickListener {
            val strUser: String = user.text.toString()
            val strPass: String = pass.text.toString()
            val strcnfPass: String = cnfpass.text.toString()
            val stremail: String = email.text.toString()
            if (stremail.isNotEmpty()) {
                if (strPass.isNotEmpty()) {
                    if (strUser.isNotEmpty()) {
                        if (strcnfPass.isNotEmpty()) {
                            if (strPass == strcnfPass) {
                                if (strPass.contains('1') || strPass.contains('2') || strPass.contains('3') || strPass.contains('4') || strPass.contains('5') || strPass.contains('6') || strPass.contains('7') || strPass.contains('8') || strPass.contains('9') || strPass.contains('0')) {
                                    if (stremail.contains('@') && (stremail.contains(".com") || stremail.contains(".it"))) {
                                        if (strUser.length < 15 && stremail.length < 25) {
                                            if(strUser == userDao.checkUser(strUser)){
                                                Toast.makeText(this, "Esiste già un Utente con lo stesso Username :(", Toast.LENGTH_SHORT)
                                                    .show()
                                            } else {

                                                newUser = User(strPass, stremail, strUser)
                                                userDao.insertUser(newUser)
                                                val intentDone = Intent(this, SignUpDone::class.java)
                                                myRef.child(newUser.user).child("Username").setValue(newUser.user)
                                                myRef.child(newUser.user).child("Email").setValue(newUser.email)
                                                myRef.child(newUser.user).child("Password").setValue(newUser.password)
                                                startActivity(intentDone)
                                            }
                                        } else {
                                            Toast.makeText(this, "I valori inseriti non sono corretti!", Toast.LENGTH_SHORT)
                                                .show()
                                        }
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
                        Toast.makeText(this, "Inserisci un nome Utente!", Toast.LENGTH_SHORT)
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
