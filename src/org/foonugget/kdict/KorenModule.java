
package org.foonugget.kdict;

import org.foonugget.kdict.data.AppDatabaseOpenHelper;
import org.foonugget.kdict.data.DictionaryDatabaseOpenHelper;
import org.foonugget.kdict.ui.SearchHistoryListAdapter;
import org.foonugget.kdict.ui.WordSearchListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;

public class KorenModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(WordSearchListAdapter.Factory.class));
        install(new FactoryModuleBuilder().build(SearchHistoryListAdapter.Factory.class));
    }

    @Provides
    @Named("sharedPreferences")
    SharedPreferences sharedPrefs(Context ctx) {
        return ctx.getSharedPreferences(KorenModule.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Provides
    @Named("AppDB")
    SQLiteDatabase appDB(AppDatabaseOpenHelper helper) {
        return helper.getWritableDatabase();
    }

    @Provides
    @Named("DictDB")
    SQLiteDatabase dictDB(DictionaryDatabaseOpenHelper helper) {
        return helper.openDatabase();
    }

}
