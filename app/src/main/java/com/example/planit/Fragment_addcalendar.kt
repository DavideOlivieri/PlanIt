package com.example.planit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calendario.R

class Fragment_addcalendar : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Infla il layout del fragment
        val rootView = inflater.inflate(R.layout.fragment_addcalendar, container, false)

        return rootView
    }
}
