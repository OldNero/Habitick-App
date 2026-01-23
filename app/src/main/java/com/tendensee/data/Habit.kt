package com.tendensee.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SchedulingType {
    DAILY,
    WEEKLY_FREQUENCY,
    SPECIFIC_DAYS
}

enum class GoalType {
    AT_LEAST,
    AT_MOST
}

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val icon: String = "default",
    val schedulingType: SchedulingType = SchedulingType.DAILY,
    val frequency: Int = 1, // For WEEKLY_FREQUENCY (e.g., 3 times a week)
    val daysOfWeek: String = "", // For SPECIFIC_DAYS (e.g., "1,3,5" for Mon, Wed, Fri)
    val goalType: GoalType = GoalType.AT_LEAST,
    val goalTarget: Float = 1.0f,
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)
