
package org.foonugget.kdict.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppDatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String NAME = "cache.db";

    @Inject
    public AppDatabaseOpenHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE search_history (id INTEGER PRIMARY KEY, search_string TEXT)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        /* no upgrades yet */
    }

}
