package com.saishaddai.flashcards

import android.app.Application
import com.saishaddai.flashcards.di.appModule
import com.saishaddai.flashcards.logging.ReleaseTree
import nl.dionsegijn.konfetti.compose.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class FlashcardsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //Timber Logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@FlashcardsApplication)
            modules(appModule)
        }
    }
}
