package com.tendensee.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun addHabit(
        title: String,
        description: String = "",
        schedulingType: SchedulingType = SchedulingType.DAILY,
        frequency: Int = 1,
        daysOfWeek: String = "",
        goalType: GoalType = GoalType.AT_LEAST,
        goalTarget: Float = 1.0f
    ) {
        habitDao.insertHabit(
            Habit(
                title = title,
                description = description,
                schedulingType = schedulingType,
                frequency = frequency,
                daysOfWeek = daysOfWeek,
                goalType = goalType,
                goalTarget = goalTarget
            )
        )
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun toggleHabitCompletion(
        habitId: Int,
        date: LocalDate,
        isCompleted: Boolean,
        value: Float = 1.0f,
        note: String? = null
    ) {
        val timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (isCompleted) {
            habitDao.insertRecord(
                HabitRecord(
                    habitId = habitId,
                    timestamp = timestamp,
                    value = value,
                    note = note
                )
            )
        } else {
            habitDao.deleteRecord(habitId, timestamp)
        }
    }
    
    fun getRecordsForDate(date: LocalDate): Flow<List<HabitRecord>> {
        val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        return habitDao.getAllRecords(start, end)
    }

    fun getRecordsInRange(start: Long, end: Long): Flow<List<HabitRecord>> {
        return habitDao.getAllRecords(start, end)
    }
}
