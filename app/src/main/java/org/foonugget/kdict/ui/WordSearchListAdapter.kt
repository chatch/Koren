package org.foonugget.kdict.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import org.foonugget.kdict.R
import org.foonugget.kdict.data.WordMatch
import org.foonugget.kdict.tts.TtsManager

class WordSearchListAdapter(
    private val context: Context,
    private val matchesList: List<WordMatch>,
    private val ttsManager: TtsManager
) : BaseAdapter() {

    override fun getCount(): Int = matchesList.size

    override fun getItem(position: Int): WordMatch = matchesList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val match = getItem(position)
        val matchView = LayoutInflater.from(context)
            .inflate(R.layout.word_match, parent, false) as LinearLayout

        val wordTv = matchView.findViewById<TextView>(R.id.matchWord)
        wordTv.text = match.word

        val defTv = matchView.findViewById<TextView>(R.id.matchDefinition)
        defTv.text = match.translation

        val speakWordBtn = matchView.findViewById<ImageButton>(R.id.speakWordBtn)
        speakWordBtn.setOnClickListener {
            ttsManager.speak(match.word)
        }

        val speakDefBtn = matchView.findViewById<ImageButton>(R.id.speakDefBtn)
        speakDefBtn.setOnClickListener {
            ttsManager.speak(match.translation)
        }

        matchView.setOnClickListener {
            val intent = android.content.Intent(context, TranslationsActivity::class.java)
            intent.putExtra(context.getString(R.string.intentVarWord), match.word)
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        return matchView
    }

    class Factory(private val context: Context, private val ttsManager: TtsManager) {
        fun create(matchesList: List<WordMatch>): WordSearchListAdapter {
            return WordSearchListAdapter(context, matchesList, ttsManager)
        }
    }
}
