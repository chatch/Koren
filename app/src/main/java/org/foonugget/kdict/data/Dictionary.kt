package org.foonugget.kdict.data

import android.database.sqlite.SQLiteDatabase

class Dictionary(
    private val mDictDB: SQLiteDatabase
) {
    fun wordSearch(word: String): List<WordMatch> {
        val trimmed = word.trim()
        if (trimmed.isEmpty()) return emptyList()
        val matches = mutableListOf<WordMatch>()
        matches.addAll(findKoreanByEnglishExact(trimmed))
        matches.addAll(findEnglishByKoreanExact(trimmed))
        return matches
    }

    fun wordSearchEach(word: String): List<WordMatch> {
        val trimmed = word.trim()
        if (trimmed.isEmpty()) return emptyList()
        val matches = mutableListOf<WordMatch>()
        for (i in 0 until trimmed.length) {
            val ch = trimmed.substring(i, i + 1)
            matches.addAll(findKoreanByEnglishExact(ch))
            matches.addAll(findEnglishByKoreanExact(ch))
        }
        return matches
    }

    fun wordSearchContains(word: String): List<WordMatch> {
        val trimmed = word.trim()
        if (trimmed.isEmpty()) return emptyList()
        val matches = mutableListOf<WordMatch>()
        matches.addAll(findKoreanByEnglishFuzzy(trimmed))
        matches.addAll(findEnglishByKoreanFuzzy(trimmed))
        return matches
    }

    fun findKoreanByEnglishFuzzy(word: String): List<WordMatch> {
        return getWordMatches(
            "SELECT word, definition FROM english_korean WHERE word LIKE ?",
            arrayOf("%$word%")
        )
    }

    fun findEnglishByKoreanFuzzy(word: String): List<WordMatch> {
        return getWordMatches(
            "SELECT word, definition FROM korean_english WHERE word LIKE ?",
            arrayOf("%$word%")
        )
    }

    fun findKoreanByEnglishExact(word: String): List<WordMatch> {
        return getWordMatches(
            "SELECT word, definition FROM english_korean WHERE word = ?",
            arrayOf(word)
        )
    }

    fun findEnglishByKoreanExact(word: String): List<WordMatch> {
        return getWordMatches(
            "SELECT word, definition FROM korean_english WHERE word = ?",
            arrayOf(word)
        )
    }

    private fun getWordMatches(sql: String, selectionArgs: Array<String>): List<WordMatch> {
        val matches = mutableListOf<WordMatch>()
        mDictDB.rawQuery(sql, selectionArgs).use { cursor ->
            while (cursor.moveToNext()) {
                val word = cursor.getString(0)
                val translation = cursor.getString(1)
                matches.add(WordMatch(word, translation))
            }
        }
        return matches
    }
}
