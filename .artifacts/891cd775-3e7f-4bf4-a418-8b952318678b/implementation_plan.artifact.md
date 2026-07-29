# Implementation Plan - Fix SettingsViewModel WorkManager Dependency

This plan addresses the `IllegalStateException` in `SettingsViewModelTest` caused by a hard dependency on `WorkManager` via `WorkerUtils`. I will decouple the reminder scheduling logic into an interface to make it mockable in unit tests.

## User Review Required

> [!IMPORTANT]
> This change introduces a new interface `ReminderScheduler` and refactors `SettingsViewModel` to use constructor injection for this dependency. This improves the app's architecture but requires updating the Koin dependency injection module.

## Proposed Changes

### Reminder Scheduling

#### [NEW] [ReminderScheduler.kt](file:///Users/sai/Projects/Flashcards-2/app/src/main/java/com/saishaddai/flashcards/worker/ReminderScheduler.kt)
- Define `ReminderScheduler` interface with `scheduleDailyReminder` and `cancelDailyReminder` methods.
- Provide a `WorkManagerReminderScheduler` implementation that delegates to the existing `WorkerUtils`.

### Dependency Injection

#### [MODIFY] [AppModule.kt](file:///Users/sai/Projects/Flashcards-2/app/src/main/java/com/saishaddai/flashcards/di/AppModule.kt)
- Add a provider for `ReminderScheduler` using `WorkManagerReminderScheduler`.

### ViewModel Refactoring

#### [MODIFY] [SettingsViewModel.kt](file:///Users/sai/Projects/Flashcards-2/app/src/main/java/com/saishaddai/flashcards/viewmodel/SettingsViewModel.kt)
- Update the constructor to accept a `ReminderScheduler` dependency.
- Replace direct calls to `WorkerUtils` with calls to the injected `reminderScheduler`.

### Unit Test Fixes

#### [MODIFY] [SettingsViewModelTest.kt](file:///Users/sai/Projects/Flashcards-2/app/src/test/java/com/saishaddai/flashcards/viewmodel/SettingsViewModelTest.kt)
- Mock the `ReminderScheduler` interface.
- Pass the mock to the `SettingsViewModel` constructor.
- This removes the need for `WorkManager` initialization during unit testing.

## Verification Plan

### Automated Tests
- Run unit tests for `SettingsViewModel`:
  `./gradlew testDebugUnitTest --tests com.saishaddai.flashcards.viewmodel.SettingsViewModelTest`
- Ensure the project compiles successfully.

### Manual Verification
- Deploy the app and toggle study reminders in Settings to ensure they are still being scheduled correctly via `WorkManager`.
