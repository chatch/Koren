package org.foonugget.kdict

import android.app.Application
import org.foonugget.kdict.tts.TtsManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class KorenApp : Application() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KorenApp)
            modules(appModule, dataModule)
        }
        ttsManager = GlobalContext.get().get()
    }

    override fun onTerminate() {
        ttsManager.shutdown()
        super.onTerminate()
    }
}
