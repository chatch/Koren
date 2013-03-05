
package org.foonugget.kdict.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SearchHistoryListAdapter extends BaseAdapter {
    @SuppressWarnings("unused")
    private final static String TAG = SearchHistoryListAdapter.class.getSimpleName();

    private final Context mContext;

    private List<String> mHistory;

    private OnClickListener mClickSearchHistoryListener;

    /**
     * Creation factory
     */
    public interface Factory {
        SearchHistoryListAdapter create(List<String> matchesList, OnClickListener clickListener);
    }

    @Inject
    public SearchHistoryListAdapter(Context context, @Assisted
    List<String> historyList,
            @Assisted
            OnClickListener clickSearchHistoryEntry) {
        mContext = context;
        mHistory = historyList;
        mClickSearchHistoryListener = clickSearchHistoryEntry;

        if (mHistory == null || mHistory.size() == 0) {
            mHistory.add("");
        }
    }

    @Override
    public int getCount() {
        return mHistory.size();
    }

    @Override
    public String getItem(int position) {
        return mHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String searchString = getItem(position);
        TextView textView = new TextView(mContext);
        textView.setText(searchString);
        textView.setTextSize(30);
        textView.setOnClickListener(mClickSearchHistoryListener);
        return textView;
    }

}
