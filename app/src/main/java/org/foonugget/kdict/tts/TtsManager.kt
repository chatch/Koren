package org.foonugget.kdict.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import java.util.Locale
import org.foonugget.kdict.R

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val appContext: Context = context.applicationContext
    private val tts: TextToSpeech = TextToSpeech(appContext, this)
    private var isReady = false
    private var koreanAvailable = false
    private var englishAvailable = false

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            isReady = false
            showUnavailableMessage()
            return
        }
        isReady = true
        koreanAvailable = tts.isLanguageAvailable(Locale.KOREAN) >= TextToSpeech.LANG_AVAILABLE
        englishAvailable = tts.isLanguageAvailable(Locale.US) >= TextToSpeech.LANG_AVAILABLE
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                isSpeaking = false
                lastSpokenText = null
            }
            override fun onError(utteranceId: String?) {
                isSpeaking = false
                lastSpokenText = null
            }
            override fun onStart(utteranceId: String?) {}
        })
        pendingText?.let {
            speakInternal(it)
            pendingText = null
        }
    }

    private var isSpeaking = false
    private var lastSpokenText: String? = null
    private var pendingText: String? = null

    fun speak(text: String) {
        if (!isReady) {
            pendingText = text
        } else if (text.isNotBlank()) {
            if (isSpeaking && text == lastSpokenText) {
                stop()
            } else {
                speakInternal(text)
            }
        }
    }

    fun stop() {
        tts.stop()
        isSpeaking = false
        lastSpokenText = null
    }

    private fun speakInternal(text: String) {
        val isKorean = text.contains(KOREAN_REGEX)
        val languageName = if (isKorean) "Korean" else "English"
        val locale = if (isKorean) Locale.KOREAN else Locale.US
        val langAvailable = if (isKorean) koreanAvailable else englishAvailable

        if (!langAvailable) {
            showLanguageUnavailableMessage(languageName)
            return
        }

        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            showLanguageUnavailableMessage(languageName)
            return
        }

        isSpeaking = true
        lastSpokenText = text
        tts.setSpeechRate(0.8f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
    }

    fun isAvailable(): Boolean = isReady

    fun shutdown() {
        tts.stop()
        tts.shutdown()
        isReady = false
    }

    private fun showUnavailableMessage() {
        Toast.makeText(
            appContext,
            appContext.getString(R.string.tts_not_available),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLanguageUnavailableMessage(language: String) {
        Toast.makeText(
            appContext,
            appContext.getString(R.string.tts_language_not_available, language),
            Toast.LENGTH_LONG
        ).show()
    }

    fun setUtteranceProgressListener(listener: UtteranceProgressListener) {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                isSpeaking = false
                lastSpokenText = null
                listener.onDone(utteranceId)
            }
            override fun onError(utteranceId: String?) {
                isSpeaking = false
                lastSpokenText = null
                listener.onError(utteranceId)
            }
            override fun onStart(utteranceId: String?) {
                listener.onStart(utteranceId)
            }
        })
    }

    fun resetSpeakingState() {
        isSpeaking = false
        lastSpokenText = null
    }

    companion object {
        private val KOREAN_REGEX = Regex("[\\uAC00-\\uD7AF\\u1100-\\u11FF\\u3130-\\u318F]")
        private const val UTTERANCE_ID = "koren_tts"
    }
}
