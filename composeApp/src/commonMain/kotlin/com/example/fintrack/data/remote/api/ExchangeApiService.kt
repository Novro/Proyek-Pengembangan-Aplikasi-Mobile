package com.example.fintrack.data.remote.api

import com.example.fintrack.core.network.safeApiCall
import com.example.fintrack.core.network.NetworkResult
import com.example.fintrack.core.network.HttpClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class FrankfurterResponse(
    @SerialName("amount") val amount: Double,
    @SerialName("base") val base: String,
    @SerialName("date") val date: String,
    @SerialName("rates") val rates: Map<String, Double>
)

class ExchangeApiService(private val client: HttpClient) {

    companion object {
        private const val BASE_URL = "https://api.frankfurter.app"
        private val ALL_CURRENCIES = listOf("USD", "IDR", "EUR", "GBP", "JPY", "SGD", "AUD")
    }

    suspend fun getLatestRates(
        baseCurrency: String = "USD"
    ): NetworkResult<FrankfurterResponse> = safeApiCall {
        val targetCurrencies = ALL_CURRENCIES.filter { it != baseCurrency }.joinToString(",")
        val urlString = "$BASE_URL/latest?from=$baseCurrency&to=$targetCurrencies"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val responseText = connection.inputStream.bufferedReader().use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString<FrankfurterResponse>(responseText)
    }
}
