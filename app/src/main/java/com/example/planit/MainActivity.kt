package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.calendario.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import roomData.User
import roomData.UserDatabase

//val db: FirebaseFirestore= FirebaseFirestore.getInstance()
var email: String? = null
var username: String? = null
var password: String? = null




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)


        //inizializzazione variabili
        val noAccount = findViewById<TextView>(R.id.sign_up_view)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val user = findViewById<EditText>(R.id.username)
        val pass = findViewById<EditText>(R.id.password)

        val userDao = UserDatabase.getInstance(application).dao()

        //getAllUsersDataFromFirebase()




        // passaggio alla schermata di signUp
        noAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        // controllo valori inseriti
        btnLogin.setOnClickListener {
            val strUser: String = user.text.toString()
            /*// Chiamata alla funzione per ottenere i dati da Firebase
            if(strUser!=userDao.checkPass(strUser)?.user){
                //getUserDataFromFirebase(strUser)
            }*/
            val strPass: String = pass.text.toString()
            if (strPass.isNotEmpty()) {
                if (strUser.isNotEmpty()) {
                    if (strUser.length < 15) {
                        getUserDataFromFirebase(strUser)
                        // Condizione essenziale per evitare un java.lang.NullPointerException generato dal fatto che non sono presenti Utenti nel Database con lo stesso username
                        if(userDao.checkPass(strUser) != null || username !=null){
                            if((strUser == userDao.checkPass(strUser).user || strUser ==username) && (strPass == userDao.checkPass(strUser).password || strPass == password)){
                                val intentLogin = Intent(this, Home::class.java)
                                intentLogin.putExtra("Username", strUser)
                                startActivity(intentLogin)
                            } else {
                                Toast.makeText(this, "Lo Username o la Password non sono corretti!", Toast.LENGTH_SHORT).show()
                            }
                        } else{
                            Toast.makeText(this, "Non sei registrato!", Toast.LENGTH_SHORT).show()
                            val intentLogin = Intent(this, SignUp::class.java)
                            startActivity(intentLogin)
                            }
                    } else {
                        Toast.makeText(this,"I valori inseriti non sono corretti!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Inserire lo User!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Inserire la Password!", Toast.LENGTH_SHORT).show()
            }

        }

    }


private fun getUserDataFromFirebase(userId: String) {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.reference.child("users").child(userId)

    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val userData = snapshot.getValue(User::class.java)

            userData?.let { user ->
                val userDao = UserDatabase.getInstance(application).dao()

                if (userDao.checkPass(user.user) == null) {
                    val newUser = User(user.password, user.email, user.user)
                    userDao.insertUser(newUser)
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


