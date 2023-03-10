package com.example.news.repository

import com.example.news.Article
import com.example.news.api.NewsAPIHelper
import com.example.news.database.Dao
import javax.inject.Inject

class Repository @Inject constructor(private val newsAPIHelper: NewsAPIHelper,private val newsDao: Dao) {
    suspend fun getNews(countryCode:String,pageNum:Int) =newsAPIHelper.getNews(countryCode,pageNum)
    suspend fun searchNews(searchQuery:String,pageNum: Int)=newsAPIHelper.searchForNews(searchQuery,pageNum)

    suspend fun insertArticle(article: Article){
        newsDao.insert(article)
    }

    suspend fun deletArticle(article: Article){
        newsDao.deletArticle(article)
    }

    fun getFavortis()=newsDao.getArticles()

}