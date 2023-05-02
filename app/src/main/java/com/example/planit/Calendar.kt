package com.example.planit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calendario.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.*

class Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendario)
        calendario()
    }

    fun calendario() {

        val startTimeCalendar = Calendar.getInstance()

        val materialCalendarView = findViewById<MaterialCalendarView>(R.id.calendarView)

        materialCalendarView.setOnDateChangedListener { widget, date, selected ->

            val year = date.year.toString()
            var month = (date.month+1).toString()
            var date = date.day.toString()

            if (month.toInt()<10) {
                month = "0$month"
            }

            if (date.toInt()<10) {
                date = "0$date"
            }

            val message = date +"-"+ month +"-"+ year

            // Crea un'istanza dell'intent per aprire la nuova pagina
            val intent = Intent(this, Day::class.java)

            // Passa la data selezionata all'intent
            intent.putExtra("data_selezionata", message)

            // Avvia l'attività con l'intent
            startActivity(intent)

            materialCalendarView.setTitleFormatter(TitleFormatter {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM", Locale.ITALY)
                simpleDateFormat.format(startTimeCalendar.time)
            })

            materialCalendarView.setOnMonthChangedListener { widget, date ->
                materialCalendarView.setTitleFormatter(TitleFormatter {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM", Locale.ITALY)
                    simpleDateFormat.format(date.date)

                })
            }



        }


    }
}
