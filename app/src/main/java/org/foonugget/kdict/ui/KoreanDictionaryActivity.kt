package org.foonugget.kdict.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.foonugget.kdict.R
import org.foonugget.kdict.data.Dictionary
import org.foonugget.kdict.data.SearchHistory
import org.foonugget.kdict.data.WordMatch
import org.koin.android.ext.android.inject

class KoreanDictionaryActivity : AppCompatActivity() {

    private val mDict: Dictionary by inject()
    private val mSearchHistory: SearchHistory by inject()
    private val wordSearchListAdapterFactory: WordSearchListAdapter.Factory by inject()
    private val searchHistoryListAdapterFactory: SearchHistoryListAdapter.Factory by inject()

    private lateinit var mSearchTextField: EditText
    private lateinit var mWordListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        mSearchTextField = findViewById(R.id.searchText)
        mWordListView = findViewById(R.id.wordList)
        populateSearchHistory()
    }

    private val onClickSearchHistoryEntry = View.OnClickListener { v ->
        val searchText = (v as TextView).text.toString()
        doSearch(SearchType.Exact, searchText)
        mSearchTextField.setText(searchText)
    }

    private fun populateSearchHistory() {
        val searchList = mSearchHistory.getSearchHistoryAll()
        val adapter = searchHistoryListAdapterFactory.create(searchList, onClickSearchHistoryEntry)
        mWordListView.adapter = adapter
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickContains(view: View) {
        doSearch(SearchType.Contains)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickEach(view: View) {
        doSearch(SearchType.Each)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickExact(view: View) {
        doSearch(SearchType.Exact)
    }

    fun doSearch(type: SearchType) {
        val searchText = mSearchTextField.text.toString()
        doSearch(type, searchText)
    }

    private fun doSearch(type: SearchType, searchText: String) {
        if (searchText.isBlank()) return

        val matchesList: List<WordMatch> = when (type) {
            SearchType.Contains -> mDict.wordSearchContains(searchText)
            SearchType.Each -> mDict.wordSearchEach(searchText)
            SearchType.Exact -> mDict.wordSearch(searchText)
        }

        val adapter = wordSearchListAdapterFactory.create(matchesList)
        mWordListView.adapter = adapter
        mSearchHistory.addSearchWordToHistory(searchText.trim())
    }

    enum class SearchType {
        Exact,
        Contains,
        Each
    }
}
