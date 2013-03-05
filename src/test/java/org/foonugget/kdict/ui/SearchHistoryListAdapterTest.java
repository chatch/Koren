
package org.foonugget.kdict.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.activity.RoboActivity;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SearchHistoryListAdapterTest {

    protected Context mContext = new RoboActivity();
    private ArrayList<String> mSearchList;
    private SearchHistoryListAdapter mSearchHistoryAdapter;

    private final OnClickListener mTestClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Before
    public void setUp() throws Exception {
        mSearchList = new ArrayList<String>();
        mSearchList.add("dog");
        mSearchList.add("head");
        mSearchHistoryAdapter = new SearchHistoryListAdapter(mContext, mSearchList,
                mTestClickListener);
    }

    @Test
    public void testGetCount() throws Exception {
        assertThat(mSearchHistoryAdapter.getCount(), equalTo(mSearchList.size()));
    }

    @Test
    public void testGetItem() throws Exception {
        assertThat((String) mSearchHistoryAdapter.getItem(0), equalTo("dog"));
        assertThat((String) mSearchHistoryAdapter.getItem(1), equalTo("head"));
    }

    @Test
    public void testGetItemId() throws Exception {
        assertThat(mSearchHistoryAdapter.getItemId(0), equalTo(0L));
        assertThat(mSearchHistoryAdapter.getItemId(1), equalTo(1L));
    }

    @Test
    public void testGetView() throws Exception {
        TextView searchTextView = (TextView) mSearchHistoryAdapter.getView(1, null,
                new LinearLayout(new Activity()));
        assertThat(searchTextView.getText().toString(), equalTo("head"));
    }

}
