package com.tendensee.utils

import com.tendensee.data.HabitRecord
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object StatsCalculator {

    fun calculateCurrentStreak(records: List<HabitRecord>): Int {
        if (records.isEmpty()) return 0
        
        val dates = records.map {
            LocalDate.ofInstant(java.time.Instant.ofEpochMilli(it.timestamp), ZoneId.systemDefault())
        }.sortedDescending().distinct()

        val today = LocalDate.now()
        var currentStreak = 0
        var checkDate = today

        // If not completed today, check if it was completed yesterday to continue streak
        // If not completed yesterday either, streak is 0
        if (!dates.contains(today)) {
            checkDate = today.minusDays(1)
            if (!dates.contains(checkDate)) return 0
        }

        for (date in dates) {
            if (date == checkDate) {
                currentStreak++
                checkDate = checkDate.minusDays(1)
            } else if (date.isBefore(checkDate)) {
                break
            }
        }

        return currentStreak
    }

    fun calculateBestStreak(records: List<HabitRecord>): Int {
        if (records.isEmpty()) return 0

        val dates = records.map {
            LocalDate.ofInstant(java.time.Instant.ofEpochMilli(it.timestamp), ZoneId.systemDefault())
        }.sorted().distinct()

        var maxStreak = 0
        var currentStreak = 0
        var lastDate: LocalDate? = null

        for (date in dates) {
            if (lastDate == null || ChronoUnit.DAYS.between(lastDate, date) == 1L) {
                currentStreak++
            } else {
                maxStreak = maxOf(maxStreak, currentStreak)
                currentStreak = 1
            }
            lastDate = date
        }

        return maxOf(maxStreak, currentStreak)
    }

    fun calculateCompletionRate(records: List<HabitRecord>, days: Int): Float {
        if (days <= 0) return 0f
        val cutoff = LocalDate.now().minusDays(days.toLong())
        val recentCount = records.count {
            LocalDate.ofInstant(java.time.Instant.ofEpochMilli(it.timestamp), ZoneId.systemDefault())
                .isAfter(cutoff.minusDays(1))
        }
        return (recentCount.toFloat() / days).coerceIn(0f, 1f) * 100
    }
}
