package com.example.news.api

import javax.inject.Inject

class NewsHelperImp @Inject constructor(private val newsAPIServices: NewsAPIServices) :NewsAPIHelper {
    override suspend fun getNews() {
        newsAPIServices.getNews()
    }

    override suspend fun searchForNews(query: String) {
        newsAPIServices.searchForNews(query)
    }
}