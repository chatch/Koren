package org.foonugget.kdict.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class SearchHistory(private val mDB: SQLiteDatabase) {

    fun addSearchWordToHistory(word: String) {
        val values = ContentValues().apply {
            put("id", getNextId())
            put("search_string", word)
        }
        mDB.insert("search_history", null, values)
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
