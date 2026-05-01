package org.foonugget.kdict.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE search_history (id INTEGER PRIMARY KEY, search_string TEXT UNIQUE)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS search_history")
        db.execSQL("CREATE TABLE search_history (id INTEGER PRIMARY KEY, search_string TEXT UNIQUE)")
    }

    companion object {
        private const val VERSION = 3
        private const val NAME = "cache.db"
    }
}
