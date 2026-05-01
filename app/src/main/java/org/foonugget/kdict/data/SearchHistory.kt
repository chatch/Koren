package org.foonugget.kdict.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class SearchHistory(private val mDB: SQLiteDatabase) {

    fun addSearchWordToHistory(word: String) {
        if (!tableExists()) {
            Log.w("SearchHistory", "search_history table missing, creating it")
            mDB.execSQL("CREATE TABLE search_history (id INTEGER PRIMARY KEY, search_string TEXT UNIQUE)")
        }
        if (exists(word)) {
            Log.d("SearchHistory", "Search word already in history, skipping: $word")
            return
        }
        val values = ContentValues().apply {
            put("id", getNextId())
            put("search_string", word)
        }
        val result = mDB.insert("search_history", null, values)
        if (result == -1L) {
            Log.e("SearchHistory", "Failed to insert search word: $word")
        }
    }

    private fun exists(word: String): Boolean {
        mDB.rawQuery(
            "SELECT 1 FROM search_history WHERE search_string = ? LIMIT 1",
            arrayOf(word)
        ).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    private fun tableExists(): Boolean {
        mDB.rawQuery(
            "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='search_history'",
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) return cursor.getInt(0) > 0
        }
        return false
    }

    private fun getNextId(): Long {
        mDB.rawQuery(
            "SELECT COALESCE(MAX(id) + 1, 0) FROM search_history",
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) return cursor.getLong(0)
        }
        return 0
    }

    fun getSearchHistoryAll(): List<String> {
        if (!tableExists()) return emptyList()
        val searches = mutableListOf<String>()
        mDB.rawQuery(
            "SELECT search_string FROM search_history ORDER BY id DESC",
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                searches.add(cursor.getString(0))
            }
        }
        return searches
    }
}
