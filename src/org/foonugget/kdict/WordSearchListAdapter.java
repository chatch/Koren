
package org.foonugget.kdict;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.foonugget.kdict.R.string;
import org.foonugget.kdict.data.WordMatch;

import java.util.List;

public class WordSearchListAdapter extends BaseAdapter {
    @SuppressWarnings("unused")
    private final static String TAG = WordSearchListAdapter.class
            .getSimpleName();

    private final Context mContext;

    private final LayoutInflater mInflater;

    private List<WordMatch> mMatches;

    /**
     * Creation factory
     */
    public interface Factory {
        WordSearchListAdapter create(List<WordMatch> matchesList);
    }

    @Inject
    public WordSearchListAdapter(Context context, @Assisted
    List<WordMatch> matchesList) {
        mContext = context;
        mMatches = matchesList;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mMatches == null || mMatches.size() == 0) {
            WordMatch noMatch = new WordMatch("No matches", "");
            mMatches.add(noMatch);
        }
    }

    @Override
    public int getCount() {
        return mMatches.size();
    }

    @Override
    public WordMatch getItem(int position) {
        return mMatches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WordMatch match = getItem(position);

        LinearLayout matchView = (LinearLayout) mInflater.inflate(R.layout.word_match, null);

        TextView wordTv = (TextView) matchView.findViewById(R.id.matchWord);
        wordTv.setText(match.getWord());

        TextView defTv = (TextView) matchView.findViewById(R.id.matchDefinition);
        defTv.setText(match.getTranslation());

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TranslationsActivity.class);
                intent.putExtra(mContext.getString(string.intentVarWord), match.getWord());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        };
        matchView.setOnClickListener(clickListener);

        return matchView;
    }

}
