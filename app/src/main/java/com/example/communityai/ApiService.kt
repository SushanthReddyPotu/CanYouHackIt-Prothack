package com.example.communityai

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("summarize/")
    suspend fun summarize(@Body request: SummaryRequest): Response<SummaryResponse>

    @POST("sentiment/")
    suspend fun analyzeSentiment(@Body request: SentimentRequest): Response<SentimentResponse>
}

data class SummaryRequest(val text: String)
data class SummaryResponse(val summary: String)

data class SentimentRequest(val text: String)
data class SentimentResponse(val label: String, val score: Float)