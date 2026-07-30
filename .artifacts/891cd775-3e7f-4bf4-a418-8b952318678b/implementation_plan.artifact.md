# Implementation Plan - Testing WorkManager

This plan introduces automated testing for the `ReminderWorker` to ensure that background study reminders are correctly triggered based on user settings.

## User Review Required

> [!IMPORTANT]
> This plan adds a new test dependency `androidx.work:work-testing`. I will also need to mock Koin components during the test since `ReminderWorker` uses `KoinComponent`.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/sai/Projects/Flashcards-2/gradle/libs.versions.toml)
- Add `androidx-work-testing` library definition.

#### [MODIFY] [app/build.gradle.kts](file:///Users/sai/Projects/Flashcards-2/app/build.gradle.kts)
- Add `androidTestImplementation(libs.androidx.work.testing)` dependency.

### Instrumented Tests

#### [NEW] [ReminderWorkerTest.kt](file:///Users/sai/Projects/Flashcards-2/app/src/androidTest/java/com/saishaddai/flashcards/worker/ReminderWorkerTest.kt)
- Create a test class in `androidTest`.
- Use `TestListenableWorkerBuilder` to run the `ReminderWorker`.
- Mock `SettingsRepository` to test both enabled and disabled reminder scenarios.
- Verify that `doWork()` returns `ListenableWorker.Result.success()`.

## Verification Plan

### Automated Tests
- Run the new worker test:
  `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.saishaddai.flashcards.worker.ReminderWorkerTest`

### Manual Verification
- I'll provide `adb` commands in the walkthrough to manually trigger the worker on a real device for visual notification verification.
