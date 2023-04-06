package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class SignUpDone : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_done)

        val tapScreen = findViewById<RelativeLayout>(R.id.activity_tap)

        tapScreen.setOnClickListener{
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}