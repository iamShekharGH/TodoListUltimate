package com.shekhargh.todolistultimate.util

import com.shekhargh.todolistultimate.data.Priority
import com.shekhargh.todolistultimate.data.TodoTaskItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDateTime.toSimpleDateString(): String {

    val day = this.dayOfMonth
    val suffix = when {
        (day % 10 == 1) && (day != 11) -> "st"
        (day % 10 == 2) && (day != 12) -> "nd"
        (day % 10 == 3) && (day != 13) -> "rd"
        else -> "th"
    }
    val pattern = "d'$suffix' MMM yy"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.US)

    return this.format(formatter)
}
fun LocalDateTime.toSimpleTimeString(): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
    return this.format(formatter)
}

val dummyTasks = listOf(
    TodoTaskItem(
        1,
        "Buy Groceries",
        "Pick up milk, eggs, and organic kale",
        false,
        LocalDateTime.now().plusDays(1),
        Priority.MEDIUM,
        listOf("Shopping", "Home")
    ),
    TodoTaskItem(
        2,
        "Workout",
        "45-minute HIIT session at the gym",
        true,
        LocalDateTime.now().minusHours(2),
        Priority.HIGH,
        listOf("Health")
    ),
    TodoTaskItem(
        3,
        "Finish Android Project",
        "Complete the Room database implementation",
        false,
        LocalDateTime.now().plusDays(3),
        Priority.HIGH,
        listOf("Work", "Coding")
    ),
    TodoTaskItem(
        4,
        "Call Mom",
        "Weekly catch-up call",
        false,
        LocalDateTime.now().plusHours(5),
        Priority.LOW,
        listOf("Personal")
    ),
    TodoTaskItem(
        5,
        "Water Plants",
        "Check the soil for the snake plant and lilies",
        true,
        LocalDateTime.now().minusDays(1),
        Priority.LOW,
        listOf("Home")
    ),
    TodoTaskItem(
        6,
        "Book Flight",
        "Check prices for the summer trip to Tokyo",
        false,
        LocalDateTime.now().plusWeeks(1),
        Priority.MEDIUM,
        listOf("Travel")
    ),
    TodoTaskItem(
        7,
        "Read 20 Pages",
        "Continue reading 'Clean Architecture'",
        false,
        LocalDateTime.now().plusDays(1),
        Priority.LOW,
        listOf("Learning")
    ),
    TodoTaskItem(
        8,
        "Dentist Appointment",
        "Annual check-up and cleaning",
        false,
        LocalDateTime.now().plusDays(4),
        Priority.HIGH,
        listOf("Health")
    ),
    TodoTaskItem(
        9,
        "Pay Electricity Bill",
        "Due by the end of the month",
        true,
        LocalDateTime.now().minusDays(2),
        Priority.HIGH,
        listOf("Finance")
    ),
    TodoTaskItem(
        10,
        "Car Service",
        "Oil change and tire rotation",
        false,
        LocalDateTime.now().plusDays(10),
        Priority.MEDIUM,
        listOf("Maintenance")
    ),
    TodoTaskItem(
        11,
        "Clean Kitchen",
        "Deep clean the oven and fridge",
        false,
        LocalDateTime.now().plusHours(12),
        Priority.MEDIUM,
        listOf("Home")
    ),
    TodoTaskItem(
        12,
        "Review PRs",
        "Go through the pending pull requests on GitHub",
        false,
        LocalDateTime.now().plusHours(2),
        Priority.HIGH,
        listOf("Work")
    ),
    TodoTaskItem(
        13,
        "Meditation",
        "10-minute mindfulness session",
        true,
        LocalDateTime.now().minusHours(5),
        Priority.LOW,
        listOf("Health", "Mindfulness")
    ),
    TodoTaskItem(
        14,
        "Submit Expenses",
        "Upload receipts for the business trip",
        false,
        LocalDateTime.now().plusDays(2),
        Priority.MEDIUM,
        listOf("Work", "Finance")
    ),
    TodoTaskItem(
        15,
        "Dog Walk",
        "Take Max to the park for an hour",
        false,
        LocalDateTime.now().plusHours(1),
        Priority.MEDIUM,
        listOf("Personal", "Pets")
    ),
    TodoTaskItem(
        16,
        "Laundry",
        "Wash and fold the darks",
        false,
        LocalDateTime.now().plusHours(8),
        Priority.LOW,
        listOf("Home")
    ),
    TodoTaskItem(
        17,
        "Update Portfolio",
        "Add the latest project to the website",
        false,
        LocalDateTime.now().plusWeeks(2),
        Priority.LOW,
        listOf("Career")
    ),
    TodoTaskItem(
        18,
        "Meeting with Client",
        "Discuss project milestones and timeline",
        false,
        LocalDateTime.now().plusDays(1),
        Priority.HIGH,
        listOf("Work")
    ),
    TodoTaskItem(
        19,
        "Buy Birthday Gift",
        "Get something for Sarah's party",
        false,
        LocalDateTime.now().plusDays(5),
        Priority.MEDIUM,
        listOf("Social")
    ),
    TodoTaskItem(
        20,
        "Study Kotlin Coroutines",
        "Watch the advanced tutorial series",
        true,
        LocalDateTime.now().minusDays(3),
        Priority.HIGH,
        listOf("Learning", "Coding")
    ),
    TodoTaskItem(
        21,
        "Plan Weekend Hike",
        "Find a trail with a good view",
        false,
        LocalDateTime.now().plusDays(4),
        Priority.LOW,
        listOf("Travel", "Health")
    )
)
