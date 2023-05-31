package com.example.planit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.calendario.R
import roomData.UserDatabase

class todayeventFragment : Fragment(R.layout.activity_todayevent) {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.activity_todayevent, container, false)
        val bundle = arguments
        val data = bundle?.getString("Data")
        val ins_data = view.findViewById<TextView>(R.id.Data)
        ins_data.setText(data)


        val userDao = UserDatabase.getInstance(requireContext()).dao()
        val events = userDao.getEventsByDate(data)
        // val events = data?.let { userDao.getEventsByDate(it) }
            for (i in events.indices) {
                val card = addCard(events[i].titolo,events[i].orario_inizio,events[i].orario_fine,events[i].descrizione, view)
                card?.setTag(events[i].id)
            }




        val indietro = view.findViewById<Button>(R.id.indietro)
        indietro.setOnClickListener {
            requireActivity().recreate()
        }
        return view
    }

    private fun addCard(titolo: String,inizio: String,fine: String,descrizione: String,view: View): CardView? {
        val linear = view.findViewById<LinearLayout>(R.id.linearlayout)
        val inflater = LayoutInflater.from(requireContext())
        val card_layout = inflater.inflate(R.layout.event_view, null)
        val card = card_layout.findViewById<CardView>(R.id.card)
        val parentOfChild: ViewGroup? = card.parent as? ViewGroup
        parentOfChild?.removeView(card)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.background_event)
        card.background = drawable

        //inserisco all'interno selle textview i valori che prendo dal database per ogni evento
        val titolo_card = card.findViewById<TextView>(R.id.titolo)
        titolo_card.setText(titolo)
        val inizio_card = card.findViewById<TextView>(R.id.orario_i)
        inizio_card.setText(inizio)
        val fine_card = card.findViewById<TextView>(R.id.orario_f)
        fine_card.setText(fine)
        val descrizione_card = card.findViewById<TextView>(R.id.descrizione)
        descrizione_card.setText(descrizione)

        if (linear != null) {
            linear.addView(card)
        }
        return card
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