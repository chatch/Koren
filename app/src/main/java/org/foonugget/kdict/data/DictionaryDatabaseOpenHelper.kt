package org.foonugget.kdict.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DictionaryDatabaseOpenHelper(
    private val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private var mDB: SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase) = Unit

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    fun openDatabase(): SQLiteDatabase {
        if (mDB != null && mDB!!.isOpen) {
            return mDB!!
        }

        var bOpenFailed = false
        try {
            mDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException) {
            Log.i(TAG, "Exception opening database. Assuming it's because it does not exist.", e)
            bOpenFailed = true
            mDB?.close()
            mDB = null
        }

        if (bOpenFailed) {
            Log.i(TAG, "copying database version $DB_VERSION")
            try {
                val dbInput: InputStream = context.assets.open(DB_NAME)
                val dbDir = File(DB_DIR)
                dbDir.mkdirs()
                val dbOutput: OutputStream = FileOutputStream(DB_FULL_PATH, false)
                copyDatabase(dbInput, dbOutput)
            } catch (e: IOException) {
                throw IllegalStateException("unable to install database ...", e)
            }
            mDB = readableDatabase
        }

        return mDB!!
    }

    private fun copyDatabase(dbInput: InputStream, dbOutput: OutputStream) {
        val newDB = readableDatabase
        newDB.close()

        val buffer = ByteArray(BUFFER_SIZE)
        var length: Int
        while (dbInput.read(buffer).also { length = it } > 0) {
            dbOutput.write(buffer, 0, length)
        }
        dbOutput.flush()
        dbOutput.close()
        dbInput.close()
    }

    @Synchronized
    override fun close() {
        mDB?.close()
        super.close()
    }

    companion object {
        private val TAG = DictionaryDatabaseOpenHelper::class.java.simpleName
        private const val DB_VERSION = 2
        private const val BUFFER_SIZE = 1024
        private val PACKAGE_NAME = DictionaryDatabaseOpenHelper::class.java.packageName
            .replace(".data", "")
        private val DB_DIR = "/data/data/$PACKAGE_NAME/databases/"
        private const val DB_NAME = "dict.ogg"
        private val DB_FULL_PATH = DB_DIR + DB_NAME
    }
}
