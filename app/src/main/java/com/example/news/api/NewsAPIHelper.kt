package com.example.news.api

import com.example.news.models.NewsResponse
import retrofit2.Response

interface NewsAPIHelper {

    suspend fun getNews(countryCode:String,pageNum:Int):Response<NewsResponse>
    suspend fun searchForNews(query:String,pageNum:Int):Response<NewsResponse>
}