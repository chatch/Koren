package org.foonugget.kdict.ui

import android.app.Activity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchHistoryListAdapterTest {

    private lateinit var mContext: Activity
    private lateinit var mSearchList: MutableList<String>
    private lateinit var mSearchHistoryAdapter: SearchHistoryListAdapter

    @Before
    fun setUp() {
        mContext = Activity()
        mSearchList = mutableListOf("dog", "head")
        mSearchHistoryAdapter = SearchHistoryListAdapter(
            mContext,
            mSearchList
        ) { }
    }

    @Test
    fun testGetCount() {
        assertEquals(2, mSearchHistoryAdapter.count)
    }

    @Test
    fun testGetItem() {
        assertEquals("dog", mSearchHistoryAdapter.getItem(0))
        assertEquals("head", mSearchHistoryAdapter.getItem(1))
    }

    @Test
    fun testGetItemId() {
        assertEquals(0L, mSearchHistoryAdapter.getItemId(0))
        assertEquals(1L, mSearchHistoryAdapter.getItemId(1))
    }
}
