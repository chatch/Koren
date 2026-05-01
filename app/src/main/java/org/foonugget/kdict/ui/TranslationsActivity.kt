package org.foonugget.kdict.ui

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.foonugget.kdict.R
import org.foonugget.kdict.data.Dictionary
import org.foonugget.kdict.data.WordMatch
import org.koin.android.ext.android.inject

class TranslationsActivity : AppCompatActivity() {

    private val dict: Dictionary by inject()
    private val wordSearchListAdapterFactory: WordSearchListAdapter.Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.translations)

        val translationsListView = findViewById<ListView>(R.id.translationsList)
        val wordString = intent.getStringExtra(getString(R.string.intentVarWord)) ?: ""
        val matchesList = dict.wordSearch(wordString)
        if (matchesList.isEmpty()) {
            return
        }
        val match = matchesList[0]

        val translations = match.translation.split(",")
        val allWords = mutableListOf<WordMatch>()
        for (word in translations) {
            val englishMatches = dict.findEnglishByKoreanExact(word.trim())
            allWords.addAll(englishMatches)
            Log.i(TAG, englishMatches.toString())
        }

        val adapter = wordSearchListAdapterFactory.create(matchesList)
        translationsListView.adapter = adapter
    }

    companion object {
        private val TAG = TranslationsActivity::class.java.simpleName
    }
}
