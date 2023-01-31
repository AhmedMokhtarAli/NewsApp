package com.example.news.api

import com.example.news.models.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsHelperImp @Inject constructor(private val newsAPIServices: NewsAPIServices) :NewsAPIHelper {
    override suspend fun getNews(countryCode:String,pageNum:Int): Response<NewsResponse> {
        return newsAPIServices.getNews(countryCode,pageNum)
    }

    override suspend fun searchForNews(query: String,pageNum: Int):Response<NewsResponse> {
       return newsAPIServices.searchForNews(query,pageNum)
    }
}