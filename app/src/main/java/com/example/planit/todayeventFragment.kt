package com.example.planit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendario.R

class todayeventFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflaziona il layout del Fragment
        val view = inflater.inflate(R.layout.activity_todayevent, container, false)


        // Restituisci la vista del Fragment
        return view
    }
}