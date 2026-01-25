package com.tendensee.ui.stats

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

class DayAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return DayOfWeek.of(value.toInt()).getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}
