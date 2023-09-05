package com.example.planit

import android.content.Context
import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.Calendar

// classe necessaria per aggiungere il bollino ai giorni contenenti degli eventi
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