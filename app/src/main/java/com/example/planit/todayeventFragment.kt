package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.example.calendario.R

class todayeventFragment : Fragment(R.layout.activity_todayevent) {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_todayevent, container, false)
        val bundle = arguments
        val data = bundle?.getString("Data")
        val username = bundle?.getString("username")
        val ins_data = view.findViewById<TextView>(R.id.Data)
        ins_data.setText(data)
        val indietro = view.findViewById<Button>(R.id.indietro)
        indietro.setOnClickListener {
            val intent = Intent(requireContext(), Home::class.java)
            intent.putExtra("Username",username)
            startActivity(intent)
        }
        return view
    }


    /*
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflaziona il layout del Fragment
        val view = inflater.inflate(R.layout.activity_todayevent, container, false)

        // Restituisci la vista del Fragment
        return view
    }

     */
}