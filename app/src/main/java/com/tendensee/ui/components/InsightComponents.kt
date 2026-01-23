package com.tendensee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tendensee.data.HabitRecord
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun ExtendedHeatmap(
    records: List<HabitRecord>,
    days: Int = 91, // Roughly 13 weeks
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val recordDates = records.map {
        LocalDate.ofInstant(java.time.Instant.ofEpochMilli(it.timestamp), ZoneId.systemDefault())
    }.toSet()

    // 13 columns (weeks), 7 rows (days)
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (week in 12 downTo 0) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (dayInWeek in 6 downTo 0) {
                        val date = today.minusDays((week * 7 + dayInWeek).toLong())
                        val isDayDone = recordDates.contains(date)
                        
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (isDayDone) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                )
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "3 months ago",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Today",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    subValue: String? = null,
    modifier: Modifier = Modifier
) {
    ShadcnCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            subValue?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
