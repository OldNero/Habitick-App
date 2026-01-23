package com.tendensee.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_records",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habitId"])]
)
data class HabitRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val timestamp: Long,
    val isCompleted: Boolean = true,
    val value: Float = 1.0f, // For "At least 30 mins" tracking
    val note: String? = null
)
