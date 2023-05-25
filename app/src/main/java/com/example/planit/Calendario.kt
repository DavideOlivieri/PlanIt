package com.example.planit

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Events
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.calendario.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import roomData.UserDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.HashSet
import java.util.Locale

class Calendario: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendario)
        calendario()

        val username = intent.getStringExtra("username")
        val id = intent.getLongExtra("id_calendario", 0)
        val nome_cal = findViewById<TextView>(R.id.nome_calendario)
        val btnInfo = findViewById<Button>(R.id.info_calendar)

        val userDao = UserDatabase.getInstance(application).dao()
        val current_calendar=userDao.selectCalendarbyId(id)

        nome_cal.text = current_calendar.titolo
        nome_cal.setText(current_calendar.titolo)

        val Btn_indietro = findViewById<Button>(R.id.Indietro)
        Btn_indietro.setOnClickListener{
            val intent = Intent (this, Home::class.java)
            intent.putExtra("Username",username)
            startActivity(intent)
        }

        btnInfo.setOnClickListener {
            val intent = Intent(this, Info_Calendar::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }


    fun calendario() {

        val startTimeCalendar = Calendar.getInstance()
        val id = intent.getLongExtra("id_calendario",0)
        val username = intent.getStringExtra("username")
        val userDao = UserDatabase.getInstance(application).dao()
        val current_calendar=userDao.selectCalendarbyId(id)
        val nome = current_calendar.titolo


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
            intent.putExtra("id_calendario", id)
            intent.putExtra("nome_cal",nome)
            intent.putExtra("username", username)
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
                view.setBackgroundDrawable(resources.getDrawable(R.drawable.background_day))
            }
        }
        materialCalendarView.addDecorator(currentDayDecorator)


        val dateFormat = SimpleDateFormat("dd-MM-yyyy",Locale.ITALY)
        val dates = userDao.getDatesWithEvents(id)
        val datesWithEvents = HashSet<Calendar>()
        for(dates in dates){
          val calendar = Calendar.getInstance()
            val date = dateFormat.parse(dates)
            calendar.time = date

            datesWithEvents.add(calendar)
        }

        val decorator = EventDecorator(this, datesWithEvents)
        materialCalendarView.addDecorator(decorator)

    }
}

