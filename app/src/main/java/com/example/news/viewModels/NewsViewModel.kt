package com.example.news.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.Article
import com.example.news.NewsApplication
import com.example.news.models.NewsResponse
import com.example.news.repository.Repository
import com.example.news.util.NewsResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor( application: Application ,private val repository: Repository): AppViewModel(application) {

    val news:MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    var newsResponse:NewsResponse?=null
    var news_page=1

    val searchNews:MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    var searchResponse:NewsResponse?=null
    var search_page=1

    init{getNews("us")}

     fun getNews(countryCode:String)=viewModelScope.launch {
         safeNewsCall(countryCode)
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
       safeSearchNewsCall(searchQuery)
    }

    private fun handelNewsResponse(response: Response<NewsResponse>):NewsResource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {  resultResponse ->
                news_page++
                if(newsResponse==null)
                {
                    newsResponse=resultResponse
                }
                else{
                    val oldArticles=newsResponse?.articles
                    val newArticles=resultResponse?.articles
                    oldArticles?.addAll(newArticles)
                }

                return NewsResource.Success(newsResponse ?: resultResponse)

            }
        }
        return NewsResource.Erorr(null,response.message())
    }

    private fun handelSearchResponse(response: Response<NewsResponse>):NewsResource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {  resultResponse ->
                search_page++
                if(searchResponse==null)
                {
                    searchResponse=resultResponse
                }
                else{
                    val oldArticles=newsResponse?.articles
                    val newArticles=resultResponse?.articles
                    oldArticles?.addAll(newArticles)
                }

                return NewsResource.Success(searchResponse ?: resultResponse)

            }
        }
        return NewsResource.Erorr(null,response.message())
    }

    fun getFavoritArticles()=repository.getFavortis()

    fun addToFavorit(article: Article)=viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun deletArticle(article: Article)=viewModelScope.launch {
        repository.deletArticle(article)
    }

    private suspend fun safeNewsCall(countryCode: String) {
        news.postValue(NewsResource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getNews(countryCode, news_page)
                news.postValue(handelNewsResponse(response))
            } else {
                news.postValue(NewsResource.Erorr(null, "No Internet Connection"))
            }
        }
        catch (t:Throwable){
            when (t){
                is IOException ->  news.postValue(NewsResource.Erorr(null, "Network Failure"))
                else ->  news.postValue(NewsResource.Erorr(null, "Conversion Error"))
            }
        }
    }
    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(NewsResource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchNews(searchQuery, search_page)
                searchNews.postValue(handelSearchResponse(response))
            } else {
                searchNews.postValue(NewsResource.Erorr(null, "No Internet Connection"))
            }
        }
        catch (t:Throwable){
            when (t){
                is IOException -> searchNews.postValue(NewsResource.Erorr(null, "Network Failure"))
                else ->  searchNews.postValue(NewsResource.Erorr(null, "Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<NewsApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork=connectivityManager.activeNetwork ?: return false
            val capabilties = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilties.hasTransport(TRANSPORT_WIFI) -> return true
                capabilties.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilties.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        }
        else {
            connectivityManager.activeNetworkInfo?.run {
                when(type){
                    TYPE_WIFI -> return true
                    TYPE_MOBILE -> return true
                    TYPE_ETHERNET -> return true
                    else -> return false
                }
            }
        }
        return false
    }
}