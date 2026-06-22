package com.saishaddai.flashcards.di

import androidx.room.Room
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
import com.saishaddai.flashcards.repository.impl.DataStoreSettingsRepository
import com.saishaddai.flashcards.repository.impl.RoomFlashcardRepository
import com.saishaddai.flashcards.repository.impl.RoomSessionRepository
import com.saishaddai.flashcards.repository.impl.RoomStatsRepository
import com.saishaddai.flashcards.repository.impl.RoomStudyRepository
import com.saishaddai.flashcards.repository.impl.OfflineDeckRepository
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
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().sessionSummaryDao() }
    single { get<AppDatabase>().studyDao() }
    single { get<AppDatabase>().flashcardDao() }

    // Repositories
    single<FlashcardRepository<DeckType, Flashcard>> { RoomFlashcardRepository(androidContext(), get()) }
    single<SessionRepository> { RoomSessionRepository(get()) }
    single<DeckRepository<Deck>> { OfflineDeckRepository(get(), get()) }
    single<StatsRepository> { RoomStatsRepository(get()) }
    single<SettingsRepository> { DataStoreSettingsRepository(androidContext()) }
    single<StudyRepository> { RoomStudyRepository(get(), get()) }

    // ViewModels
    viewModel { DecksViewModel(androidApplication(), get()) }
    viewModel { (deckId: Int) -> FlashcardViewModel(androidApplication(), deckId, get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { FinishSessionViewModel(get()) }
    viewModel { SettingsViewModel(androidApplication(), get()) }
}
