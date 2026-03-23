package com.farmassist.app.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSManager(context: Context, languageCode: String) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isLoaded = false
    private val locale = if (languageCode == "ta") Locale("ta", "IN") else Locale.ENGLISH

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = locale
            isLoaded = true
        }
    }

    fun speak(text: String) {
        if (isLoaded) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
