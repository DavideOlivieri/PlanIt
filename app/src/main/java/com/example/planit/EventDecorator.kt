package com.example.planit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.example.calendario.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.time.LocalDate
import java.util.Calendar

class EventDecorator(context: Context, val datesWithEvents: HashSet<Calendar>): DayViewDecorator {

     override fun shouldDecorate(day: CalendarDay): Boolean {
         return datesWithEvents.contains(day.calendar)
     }

     val color = Color.BLUE
     override fun decorate(view: DayViewFacade) {
         //view.setSelectionDrawable(drawable)
         view.addSpan(DotSpan(8f, color))
     }

}