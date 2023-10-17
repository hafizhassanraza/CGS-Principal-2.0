package com.enfotrix.cgs_principal

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CalendarDateDecorator(private val context: Context, private val day: CalendarDay, private val status:String):DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return this.day == day
    }

    override fun decorate(view: DayViewFacade) {
        when (status) {
            "Present" -> {
                ContextCompat.getDrawable(context, R.drawable.bg_present)
                    ?.let { view.setBackgroundDrawable(it) }
                view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)))
            }
            "Absent" ->{
                ContextCompat.getDrawable(context, R.drawable.bg_absent)
                    ?.let { view.setBackgroundDrawable(it) }
                view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)))
            }
            "Leave" ->{
                ContextCompat.getDrawable(context, R.drawable.bg_leave)
                    ?.let { view.setBackgroundDrawable(it) }
                view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.white)))
            }
        }
    }
}
