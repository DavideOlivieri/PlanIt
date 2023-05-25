package com.example.planit

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.calendario.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar

class EventDecorator(private val context: Context, private val datesWithEvents: HashSet<Calendar>) :
    DayViewDecorator {
    private val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.event_dot)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return datesWithEvents.contains(day.calendar)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
    }
}