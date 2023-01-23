package com.example.news.api

import com.example.news.models.NewsResponse
import com.example.news.util.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIServices {

        @GET("v2/everything")
        suspend fun getNews(
            @Query("country")
            countryCode: String = "us",
            @Query("page")
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = API_KEY
        ): Response<NewsResponse>

        @GET("v2/everything")
        suspend fun searchForNews(
            @Query("q")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = API_KEY
        ): Response<NewsResponse>

}
