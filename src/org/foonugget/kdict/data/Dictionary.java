
package org.foonugget.kdict.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    @SuppressWarnings("unused")
    private final static String TAG = Dictionary.class.getSimpleName();

    private SQLiteDatabase mDB;

    @Inject
    public Dictionary(DatabaseOpenHelper helper) {
        mDB = helper.openDatabase();
    }

    public List<WordMatch> wordSearch(String word) {
        word = word.trim();
        addSearchWordToHistory(word);

        ArrayList<WordMatch> matches = new ArrayList<WordMatch>();
        matches.addAll(findKoreanByEnglishExact(word));
        matches.addAll(findEnglishByKoreanExact(word));

        return matches;
    }

    public List<WordMatch> wordSearchEach(String word) {
        word = word.trim();
        addSearchWordToHistory(word);

        ArrayList<WordMatch> matches = new ArrayList<WordMatch>();

        for (int i = 0; i < word.length(); i++) {
            String ch = word.substring(i, i + 1);
            matches.addAll(findKoreanByEnglishExact(ch));
            matches.addAll(findEnglishByKoreanExact(ch));
        }

        return matches;
    }

    public List<WordMatch> wordSearchContains(String word) {
        word = word.trim();
        addSearchWordToHistory(word);

        ArrayList<WordMatch> matches = new ArrayList<WordMatch>();
        matches.addAll(findKoreanByEnglishFuzzy(word));
        matches.addAll(findEnglishByKoreanFuzzy(word));

        return matches;
    }

    public List<WordMatch> findKoreanByEnglishFuzzy(String word) {
        final String SQL = "SELECT word, definition FROM english_korean WHERE word LIKE '%"
                + escapeString(word) + "%'";
        return getWordMatches(SQL);
    }

    public List<WordMatch> findEnglishByKoreanFuzzy(String word) {
        final String SQL = "SELECT word, definition FROM korean_english WHERE word LIKE '%"
                + escapeString(word) + "%'";
        return getWordMatches(SQL);
    }

    public List<WordMatch> findKoreanByEnglishExact(String word) {
        final String SQL = "SELECT word, definition FROM english_korean WHERE word = '"
                + escapeString(word) + "'";
        return getWordMatches(SQL);
    }

    public List<WordMatch> findEnglishByKoreanExact(String word) {
        final String SQL = "SELECT word, definition FROM korean_english WHERE word = '"
                + escapeString(word) + "'";
        return getWordMatches(SQL);
    }

    protected String escapeString(String string) {
        return string.replaceAll("'", "''");
    }
    
    private void addSearchWordToHistory(String word) {
        final String SQL = "INSERT INTO search_history VALUES ((SELECT MAX(id)+1 FROM search_history), '" + escapeString(word) + "')";
        mDB.execSQL(SQL);
    }

    private List<WordMatch> getWordMatches(final String SQL) {
        List<WordMatch> matches = new ArrayList<WordMatch>(0);

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(SQL, null);
            if (cursor.moveToFirst() == false) {
                cursor.close();
                return matches;
            }

            while (cursor.isAfterLast() == false) {
                String word = cursor.getString(0);
                String translation = cursor.getString(1);
                WordMatch wordMatch = new WordMatch(word, translation);
                matches.add(wordMatch);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return matches;
    }

}
