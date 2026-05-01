package org.foonugget.kdict.ui

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SearchHistoryListAdapter(
    private val context: Context,
    private val historyList: List<String>,
    private val clickSearchHistoryEntry: OnClickListener
) : BaseAdapter() {

    override fun getCount(): Int = historyList.size

    override fun getItem(position: Int): String = historyList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val searchString = getItem(position)
        val textView = TextView(context)
        textView.text = searchString
        textView.textSize = TEXT_SIZE
        textView.setOnClickListener(clickSearchHistoryEntry)
        return textView
    }

    class Factory(private val context: Context) {
        fun create(
            historyList: List<String>,
            clickListener: OnClickListener
        ): SearchHistoryListAdapter {
            return SearchHistoryListAdapter(context, historyList, clickListener)
        }
    }

    companion object {
        private const val TEXT_SIZE = 30f
    }
}
