package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.datepicker.MaterialCalendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        val noAccount = findViewById<TextView>(R.id.sign_up_view)

        noAccount.setOnClickListener{
            val intent =  Intent(this,SignUp::class.java)
            startActivity(intent)
    }
    }

}

