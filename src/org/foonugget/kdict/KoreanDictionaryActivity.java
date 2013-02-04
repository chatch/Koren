
package org.foonugget.kdict;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.inject.Inject;

import org.foonugget.kdict.R.id;
import org.foonugget.kdict.WordSearchListAdapter.Factory;
import org.foonugget.kdict.data.Dictionary;
import org.foonugget.kdict.data.WordMatch;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.List;

public class KoreanDictionaryActivity extends RoboActivity {

    @SuppressWarnings("unused")
    private final static String TAG = KoreanDictionaryActivity.class
            .getSimpleName();

    @InjectView(id.searchText)
    private EditText mSearchTextField;

    @InjectView(id.matchesList)
    private ListView mMatchesListView;

    @Inject
    private Dictionary mDict;

    @Inject
    private Factory wordSearchListAdapterFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClickExact(View view) {
        String word = mSearchTextField.getText().toString();
        List<WordMatch> matchesList = mDict.wordSearch(word);
        WordSearchListAdapter adapter = wordSearchListAdapterFactory.create(matchesList);
        mMatchesListView.setAdapter(adapter);
    }

    public void onClickEach(View view) {
        String word = mSearchTextField.getText().toString();
        List<WordMatch> matchesList = mDict.wordSearchEach(word);
        WordSearchListAdapter adapter = wordSearchListAdapterFactory.create(matchesList);
        mMatchesListView.setAdapter(adapter);
    }

    public void onClickContains(View view) {
        String word = mSearchTextField.getText().toString();
        List<WordMatch> matchesList = mDict.wordSearchContains(word);
        WordSearchListAdapter adapter = wordSearchListAdapterFactory.create(matchesList);
        mMatchesListView.setAdapter(adapter);
    }

}
