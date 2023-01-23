package com.example.news.repository

import com.example.news.api.NewsAPIHelper
import com.example.news.database.Dao
import javax.inject.Inject

class Repository @Inject constructor(private val newsAPIHelper: NewsAPIHelper,private val newsDao: Dao) {
    suspend fun getNews() =newsAPIHelper.getNews()
}