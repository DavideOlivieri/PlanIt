package com.example.planit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.calendario.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Calendario(nome: String, nothing: String, i: Int) : AppCompatActivity() {
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


        val sundayDecorator = SundayDecorator()
        materialCalendarView.addDecorators(sundayDecorator)

        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)

        // Imposta il colore di sfondo del giorno selezionato
        calendarView.setSelectionColor(ContextCompat.getColor(this, R.color.green))

        // Seleziona il giorno corrente
        calendarView.setCurrentDate(CalendarDay.today())

        val currentDate = CalendarDay.today()
        materialCalendarView.setCurrentDate(currentDate)

        val currentDayDecorator = object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return day == currentDate
            }

            override fun decorate(view: DayViewFacade) {
                view.setSelectionDrawable(resources.getDrawable(R.drawable.botton_plus))
            }
        }

        materialCalendarView.addDecorator(currentDayDecorator)

    }
}
