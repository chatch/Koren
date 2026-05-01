package org.foonugget.kdict

import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single {
        get<android.content.Context>().getSharedPreferences(
            KorenApp::class.java.simpleName,
            android.content.Context.MODE_PRIVATE
        )
    }
    single { org.foonugget.kdict.tts.TtsManager(get()) }
    factory { org.foonugget.kdict.ui.WordSearchListAdapter.Factory(get(), get()) }
    factory { org.foonugget.kdict.ui.SearchHistoryListAdapter.Factory(get()) }
}

val dataModule = module {
    single { org.foonugget.kdict.data.AppDatabaseOpenHelper(get()) }
    single { org.foonugget.kdict.data.DictionaryDatabaseOpenHelper(get()) }
    single(named("appDB")) { get<org.foonugget.kdict.data.AppDatabaseOpenHelper>().writableDatabase }
    single(named("dictDB")) { get<org.foonugget.kdict.data.DictionaryDatabaseOpenHelper>().openDatabase() }
    single { org.foonugget.kdict.data.Dictionary(get(named("dictDB"))) }
    single { org.foonugget.kdict.data.SearchHistory(get(named("appDB"))) }
}
