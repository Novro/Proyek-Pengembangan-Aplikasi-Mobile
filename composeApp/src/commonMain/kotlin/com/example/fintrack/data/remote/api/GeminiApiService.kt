package com.example.fintrack.data.remote.api

import com.example.fintrack.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class GeminiApiService {

    suspend fun getFinancialAdvice(
        totalExpense: Double,
        budget: Double,
        topCategory: String
    ): String = withContext(Dispatchers.IO) {
        val formattedExpense = String.format(Locale.US, "%.2f", totalExpense)
        val fallback = "Pengeluaranmu mencapai $${formattedExpense}. Tetap bijak mengelola keuangan!"

        try {
            val urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val promptText = "Kamu adalah penasihat keuangan pribadi. Bulan ini saya sudah menghabiskan $${formattedExpense} dari budget $${budget}. Pengeluaran terbesar di kategori ${topCategory}. Berikan komentar singkat, ramah, maksimal 2 kalimat. Jangan pakai markdown."

            val jsonPayload = JSONObject().apply {
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().apply {
                        put("text", promptText)
                    }))
                }))
            }.toString()

            connection.outputStream.use { os ->
                OutputStreamWriter(os, "UTF-8").use { osw ->
                    osw.write(jsonPayload)
                    osw.flush()
                }
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseString = connection.inputStream.bufferedReader().use { it.readText() }
                val responseJson = JSONObject(responseString)
                val advice = responseJson.getJSONArray("candidates")
                    .getJSONObject(0).getJSONObject("content").getJSONArray("parts")
                    .getJSONObject(0).getString("text")
                advice.trim()
            } else {
                fallback
            }
        } catch (e: Exception) {
            fallback
        }
    }
}