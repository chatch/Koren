
package org.foonugget.kdict;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.inject.Inject;

import org.foonugget.kdict.WordSearchListAdapter.Factory;
import org.foonugget.kdict.data.Dictionary;
import org.foonugget.kdict.data.WordMatch;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class TranslationsActivity extends RoboActivity {

    private final static String TAG = TranslationsActivity.class
            .getSimpleName();

    @InjectView(R.id.translationsList)
    private ListView mtranslationsListView;

    @InjectResource(R.string.intentVarWord)
    private String WORD_KEY;

    @Inject
    Dictionary dict;

    @Inject
    private Factory wordSearchListAdapterFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translations);

        Intent intent = getIntent();
        String wordString = intent.getStringExtra(WORD_KEY);
        List<WordMatch> matchesList = dict.wordSearch(wordString);
        WordMatch match = matchesList.get(0);

        // String matches = match.getTranslation();
        String[] translations = match.getTranslation().split(",");
        List<WordMatch> allWords = new ArrayList<WordMatch>();
        for (String word : translations) {
            List<WordMatch> englishMatches = dict.findEnglishByKoreanExact(word);
            allWords.addAll(englishMatches);
            Log.i(TAG, englishMatches.toString());
        }

        WordSearchListAdapter adapter = wordSearchListAdapterFactory.create(matchesList);
        mtranslationsListView.setAdapter(adapter);
    }

}
