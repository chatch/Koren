
package org.foonugget.kdict.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.foonugget.kdict.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.widget.Button;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class KoreanDictionaryActivityTest {

    private KoreanDictionaryActivity mActivity;
    private Button mEachButton;
    private Button mExactButton;
    private Button mContainsButton;

    @Before
    public void setUp() throws Exception {
        mActivity = new KoreanDictionaryActivity();
        mActivity.onCreate(null);

        mEachButton = (Button) mActivity.findViewById(R.id.eachButton);
        mExactButton = (Button) mActivity.findViewById(R.id.exactButton);
        mContainsButton = (Button) mActivity.findViewById(R.id.containsButton);
    }

    @Test
    public void shouldHaveTitle() throws Exception {
        String chartTitle = new KoreanDictionaryActivity().getResources().getString(
                R.string.app_name);
        assertThat(chartTitle, equalTo("Koren"));
    }

    @Test
    public void shouldHaveButtons() throws Exception {
        assertThat((String) mEachButton.getText(), equalTo("Each"));
        assertThat((String) mExactButton.getText(), equalTo("Exact"));
        assertThat((String) mContainsButton.getText(), equalTo("Contains"));
    }
}
