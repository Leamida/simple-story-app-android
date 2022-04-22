package com.example.storyapp.core.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import okio.IOException
import java.io.InputStreamReader

object JsonConverter {
    fun readStringFromFile(fileName: String): String {
        try {
            val applicationContext = ApplicationProvider.getApplicationContext<Context>()
            val inputStream = applicationContext.assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}