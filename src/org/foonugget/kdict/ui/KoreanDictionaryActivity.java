
package org.foonugget.kdict.ui;

import java.util.ArrayList;
import java.util.List;

import org.foonugget.kdict.R;
import org.foonugget.kdict.R.id;
import org.foonugget.kdict.data.Dictionary;
import org.foonugget.kdict.data.SearchHistory;
import org.foonugget.kdict.data.WordMatch;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;

public class KoreanDictionaryActivity extends RoboActivity {

    @SuppressWarnings("unused")
    private final static String TAG = KoreanDictionaryActivity.class
            .getSimpleName();

    enum SearchType {
        Exact,
        Contains,
        Each
    }

    @InjectView(id.searchText)
    private EditText mSearchTextField;

    /* Contains search history or search results depending on the context */
    @InjectView(id.wordList)
    private ListView mWordListView;

    @Inject
    private Dictionary mDict;

    @Inject
    private SearchHistory mSearchHistory;

    @Inject
    private WordSearchListAdapter.Factory wordSearchListAdapterFactory;

    @Inject
    private SearchHistoryListAdapter.Factory searchHistoryListAdapterFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        populateSearchHistory();
    }

    private final OnClickListener onClickSearchHistoryEntry = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String searchText = (String) ((TextView) v).getText();
            doSearch(SearchType.Exact, searchText);
            mSearchTextField.setText(searchText);
        }
    };

    private void populateSearchHistory() {
        List<String> searchList = mSearchHistory.getSearchHistoryAll();
        SearchHistoryListAdapter adapter = searchHistoryListAdapterFactory.create(searchList,
                onClickSearchHistoryEntry);
        mWordListView.setAdapter(adapter);
    }

    public void onClickContains(View view) {
        doSearch(SearchType.Contains);
    }

    public void onClickEach(View view) {
        doSearch(SearchType.Each);
    }

    public void onClickExact(View view) {
        doSearch(SearchType.Exact);
    }

    public void doSearch(SearchType type) {
        String searchText = mSearchTextField.getText().toString();
        doSearch(type, searchText);
    }

    private void doSearch(SearchType type, String searchText) {
        List<WordMatch> matchesList;
        switch (type) {
            case Contains:
                matchesList = mDict.wordSearchContains(searchText);
                break;
            case Each:
                matchesList = mDict.wordSearchEach(searchText);
                break;
            case Exact:
                matchesList = mDict.wordSearch(searchText);
                break;
            default:
                matchesList = new ArrayList<WordMatch>();
        }

        WordSearchListAdapter adapter = wordSearchListAdapterFactory.create(matchesList);
        mWordListView.setAdapter(adapter);
        mSearchHistory.addSearchWordToHistory(searchText);
    }

}
