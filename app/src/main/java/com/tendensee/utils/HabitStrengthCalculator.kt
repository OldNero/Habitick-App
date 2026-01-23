package com.tendensee.utils

import com.tendensee.data.HabitRecord
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object HabitStrengthCalculator {
    /**
     * Calculates habit strength based on recent history.
     * Formula: S_new = S_old * 0.9 + (Hit ? 10 : 0)
     * We calculate this iteratively over the last 30 days.
     */
    fun calculateStrength(records: List<HabitRecord>, daysToLookBack: Int = 30): Int {
        val today = LocalDate.now()
        val recordDates = records.map {
            LocalDate.ofInstant(java.time.Instant.ofEpochMilli(it.timestamp), ZoneId.systemDefault())
        }.toSet()

        var strength = 0.0
        for (i in daysToLookBack downTo 0) {
            val date = today.minusDays(i.toLong())
            val hit = recordDates.contains(date)
            strength = (strength * 0.9) + (if (hit) 10.0 else 0.0)
        }
        
        return strength.coerceIn(0.0, 100.0).toInt()
    }
}
