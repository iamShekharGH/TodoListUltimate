# TaskMaster

## Project Overview

TaskMaster is an offline-first Android To-Do application built utilizing strict Test-Driven Development (TDD) and Clean Architecture principles. It operates as a technical showcase for resolving complex Android edge cases, specifically focusing on inter-process communication, background execution, and reactive state synchronization across the OS.

## Core Features

* Reactive Home Screen Widget: A Jetpack Glance widget that continuously observes Room database flows to instantly reflect task completion toggles without manual query triggers.
* Background Task Scheduling: WorkManager integration to reliably execute daily task summaries and target notifications for pending deadlines.
* Cross-Process Deep Linking: Custom intent filters and URI routing to seamlessly navigate users from external widgets and notifications directly into specific Compose NavHost destinations.
* Offline-First Persistence: A Room SQLite database serving as the single source of truth for all task creations, updates, and deletions.
* Dynamic Compose UI: A fully declarative user interface featuring interactive checkboxes, dynamic theme coloring, and integrated date/time pickers.

## Architecture

* Presentation Layer: Utilizes the MVVM pattern with Jetpack Compose. ViewModels expose immutable StateFlow streams and handle user intents without holding framework references.
* Domain Layer: Contains pure Kotlin Use Cases representing isolated business rules, ensuring logic is decoupled from external data sources.
* Data Layer: Implements the Repository pattern to abstract Room DAO operations and WorkManager scheduling behind domain-level interfaces.

## Technology Stack

Kotlin, Jetpack Compose, Material Design 3, Dagger Hilt, Room Database, WorkManager, Jetpack Glance, Jetpack Compose Navigation.

## Testing Methodology

* Unit Testing: JUnit4 combined with MockK for dependency mocking across ViewModels and Use Cases.
* Flow Testing: Turbine for asserting asynchronous state emissions from Kotlin Flows.
* Coroutine Management: Custom test rules injecting UnconfinedTestDispatcher to guarantee predictable execution of suspending functions.
* Integration Testing: Robolectric utilized for verifying WorkManager constraints and worker execution on the local JVM.
