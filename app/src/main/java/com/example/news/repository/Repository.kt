package com.example.news.repository

import com.example.news.api.NewsAPIHelper
import javax.inject.Inject

class Repository @Inject constructor(private val newsAPIHelper: NewsAPIHelper) {
    suspend fun getNews() =newsAPIHelper.getNews()
}