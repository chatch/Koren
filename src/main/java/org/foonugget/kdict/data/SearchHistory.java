
package org.foonugget.kdict.data;

import java.util.ArrayList;
import java.util.List;

import org.foonugget.kdict.Util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class SearchHistory {

    @SuppressWarnings("unused")
    private final static String TAG = SearchHistory.class.getSimpleName();

    @Inject @Named("AppDB")
    private SQLiteDatabase mDB;

    public void addSearchWordToHistory(String word) {
        final String SQL = "INSERT INTO search_history " +
                "VALUES ((SELECT MAX(id)+1 FROM search_history), '" +
                Util.escapeDatabaseStringLiteral(word) + "')";
        mDB.execSQL(SQL);
    }

    public List<String> getSearchHistoryAll() {
        final String SQL = "SELECT search_string FROM search_history ORDER BY id DESC";
        List<String> searches = new ArrayList<String>(0);
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(SQL, null);
            if (cursor.moveToFirst() == false) {
                cursor.close();
                return searches;
            }

            while (cursor.isAfterLast() == false) {
                searches.add(cursor.getString(0));
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return searches;
    }

}
