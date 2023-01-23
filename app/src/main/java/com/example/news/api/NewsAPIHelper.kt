package com.example.news.api

interface NewsAPIHelper {

    suspend fun getNews()
    suspend fun searchForNews(query:String)
}