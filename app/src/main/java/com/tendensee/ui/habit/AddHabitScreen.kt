package com.tendensee.ui.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.tendensee.data.SchedulingType
import com.tendensee.data.GoalType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tendensee.ui.components.ButtonVariant
import com.tendensee.ui.components.ShadcnButton
import com.tendensee.ui.components.ShadcnInput
import com.tendensee.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    viewModel: HabitViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var schedulingType by remember { mutableStateOf(SchedulingType.DAILY) }
    var frequency by remember { mutableStateOf("1") }
    var daysOfWeek by remember { mutableStateOf("") } // e.g. "1,3,5"
    var goalType by remember { mutableStateOf(GoalType.AT_LEAST) }
    var goalTarget by remember { mutableStateOf("1.0") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Habit") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            item {
                ShadcnInput(
                    value = title,
                    onValueChange = { title = it },
                    label = "Habit Name",
                    placeholder = "e.g., Read 30 mins"
                )
            }
            
            item {
                ShadcnInput(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description (Optional)",
                    placeholder = "Why is this important?",
                    singleLine = false
                )
            }

            item {
                Text("Scheduling", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SchedulingType.values().forEach { type ->
                        ShadcnButton(
                            onClick = { schedulingType = type },
                            text = type.name.lowercase().replace("_", " ").capitalize(),
                            variant = if (schedulingType == type) ButtonVariant.Primary else ButtonVariant.Outline,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (schedulingType == SchedulingType.WEEKLY_FREQUENCY) {
                item {
                    ShadcnInput(
                        value = frequency,
                        onValueChange = { if (it.all { char -> char.isDigit() }) frequency = it },
                        label = "Times per week",
                        placeholder = "e.g., 3"
                    )
                }
            }

            if (schedulingType == SchedulingType.SPECIFIC_DAYS) {
                item {
                    ShadcnInput(
                        value = daysOfWeek,
                        onValueChange = { daysOfWeek = it },
                        label = "Specific Days (1-7)",
                        placeholder = "e.g., 1,3,5 (Mon, Wed, Fri)"
                    )
                }
            }

            item {
                Text("Goal", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalType.values().forEach { type ->
                        ShadcnButton(
                            onClick = { goalType = type },
                            text = type.name.lowercase().replace("_", " ").capitalize(),
                            variant = if (goalType == type) ButtonVariant.Primary else ButtonVariant.Outline,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                ShadcnInput(
                    value = goalTarget,
                    onValueChange = { goalTarget = it },
                    label = "Target Value",
                    placeholder = "e.g., 1.0 or 30.0"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ShadcnButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addHabit(
                                title = title,
                                description = description,
                                schedulingType = schedulingType,
                                frequency = frequency.toIntOrNull() ?: 1,
                                daysOfWeek = daysOfWeek,
                                goalType = goalType,
                                goalTarget = goalTarget.toFloatOrNull() ?: 1.0f
                            )
                            navController.popBackStack()
                        }
                    },
                    text = "Create Habit",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
