package org.foonugget.kdict

import org.koin.dsl.module

val appModule = module {
    single {
        get<android.content.Context>().getSharedPreferences(
            KorenApp::class.java.simpleName,
            android.content.Context.MODE_PRIVATE
        )
    }
    factory { org.foonugget.kdict.ui.WordSearchListAdapter.Factory(get()) }
    factory { org.foonugget.kdict.ui.SearchHistoryListAdapter.Factory(get()) }
}

val dataModule = module {
    single { org.foonugget.kdict.data.AppDatabaseOpenHelper(get()) }
    single { org.foonugget.kdict.data.DictionaryDatabaseOpenHelper(get()) }
    single { get<org.foonugget.kdict.data.AppDatabaseOpenHelper>().writableDatabase }
    single { get<org.foonugget.kdict.data.DictionaryDatabaseOpenHelper>().openDatabase() }
    single { org.foonugget.kdict.data.Dictionary(get()) }
    single { org.foonugget.kdict.data.SearchHistory(get()) }
}
