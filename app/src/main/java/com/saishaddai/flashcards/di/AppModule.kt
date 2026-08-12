package com.saishaddai.flashcards.di

import androidx.room.Room
import com.saishaddai.flashcards.data.assets.FlashcardAssetDataSource
import com.saishaddai.flashcards.data.assets.FlashcardAssetDataSourceImpl
import com.saishaddai.flashcards.data.local.AppDatabase
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.repository.SessionRepository
import com.saishaddai.flashcards.repository.StudyRepository
import com.saishaddai.flashcards.repository.impl.*
import com.saishaddai.flashcards.worker.ReminderScheduler
import com.saishaddai.flashcards.worker.WorkManagerReminderScheduler
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.FinishSessionViewModel
import com.saishaddai.flashcards.viewmodel.FlashcardViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import com.saishaddai.flashcards.viewmodel.StatsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "flashcards_db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }
    single { get<AppDatabase>().sessionSummaryDao() }
    single { get<AppDatabase>().studyDao() }
    single { get<AppDatabase>().flashcardDao() }

    // Data Sources
    single<FlashcardAssetDataSource> { FlashcardAssetDataSourceImpl(androidContext()) }
    single<ReminderScheduler> { WorkManagerReminderScheduler(androidContext()) }

    // Repositories
    single<FlashcardRepository<DeckType, Flashcard>> { LocalFlashcardRepository(get(), get()) }
    single<SessionRepository> { LocalSessionRepository(get()) }
    single<DeckRepository<Deck>> { LocalDeckRepository(get(), get()) }
    single<StatsRepository> { LocalStatsRepository(get()) }
    single<SettingsRepository> { LocalSettingsRepository(androidContext(), get(), get()) }
    single<StudyRepository> { LocalStudyRepository(get(), get()) }

    // ViewModels
    viewModel { DecksViewModel(androidApplication(), get()) }
    viewModel { (deckId: Int) -> FlashcardViewModel(androidApplication(), deckId, get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { FinishSessionViewModel(get()) }
    viewModel { SettingsViewModel(androidApplication(), get(), get()) }
}
