package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
import java.util.Calendar
import java.util.HashSet
import java.util.Locale

class Calendario: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendario)
        calendario()

        // inizializzazione variabili
        val username = intent.getStringExtra("username")
        val id = intent.getLongExtra("id_calendario", 0)
        val nomeCal = findViewById<TextView>(R.id.nome_calendario)
        val btnInfo = findViewById<Button>(R.id.info_calendar)

        val userDao = UserDatabase.getInstance(application).dao()
        val currentCalendar=userDao.selectCalendarbyId(id)

        nomeCal.text = currentCalendar.titolo
        nomeCal.setText(currentCalendar.titolo)

        // bottone indietro
        val btnIndietro = findViewById<Button>(R.id.Indietro)
        btnIndietro.setOnClickListener{
            val intent = Intent (this, Home::class.java)
            intent.putExtra("Username",username)
            startActivity(intent)
        }

        // bottone per informazioni
        btnInfo.setOnClickListener {
            val intent = Intent(this, Info_Calendar::class.java)
            intent.putExtra("id_calendario", id)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }


    //funzione necessaria per visualizzare il calendario
    fun calendario() {

        val startTimeCalendar = Calendar.getInstance()
        val id = intent.getLongExtra("id_calendario",0)
        val username = intent.getStringExtra("username")
        val userDao = UserDatabase.getInstance(application).dao()
        val currentCalendar=userDao.selectCalendarbyId(id)
        val nome = currentCalendar.titolo


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

