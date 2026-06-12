package com.example.fintrack.data.remote.api

import com.example.fintrack.core.network.HttpClient

class GeminiService(private val client: HttpClient) {
    suspend fun generateContent(
        prompt: String,
        systemPrompt: String? = null
    ): Result<String> = runCatching {
        "Mock content generation response"
    }
}

object SystemPrompts {
    val SUMMARIZER = "summarizer prompt"
    val IDEA_GENERATOR = "idea generator prompt"
    val WRITING_IMPROVER = "writing improver prompt"
    val TITLE_SUGGESTER = "title suggester prompt"
    val TRANSLATOR = "translator prompt"
}
