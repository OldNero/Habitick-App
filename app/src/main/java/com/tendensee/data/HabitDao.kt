package com.tendensee.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Insert
    suspend fun insertRecord(record: HabitRecord)

    @Query("DELETE FROM habit_records WHERE habitId = :habitId AND timestamp = :timestamp")
    suspend fun deleteRecord(habitId: Int, timestamp: Long)

    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND timestamp >= :start AND timestamp <= :end")
    fun getRecordsForHabit(habitId: Int, start: Long, end: Long): Flow<List<HabitRecord>>

    @Query("SELECT * FROM habit_records WHERE habitId = :habitId ORDER BY timestamp DESC")
    fun getAllRecordsForHabit(habitId: Int): Flow<List<HabitRecord>>

    @Query("SELECT * FROM habit_records WHERE timestamp >= :start AND timestamp <= :end")
    fun getAllRecords(start: Long, end: Long): Flow<List<HabitRecord>>
}
