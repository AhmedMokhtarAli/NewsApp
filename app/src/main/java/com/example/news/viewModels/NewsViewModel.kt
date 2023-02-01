package com.example.news.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.Repository
import com.example.news.util.NewsResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    val news:MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    val news_page=1

    val searchNews:MutableLiveData<NewsResource<NewsResponse>> = MutableLiveData()
    val search_page=1

    init{getNews("us")}

    private fun getNews(countryCode:String)=viewModelScope.launch {
        news.postValue(NewsResource.Loading())
        val response=repository.getNews(countryCode,news_page)
        news.postValue(handelNewsResponse(response))
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
        searchNews.postValue(NewsResource.Loading())
        val response=repository.searchNews(searchQuery,search_page)
        searchNews.postValue(handelSearchResponse(response))
    }

    private fun handelNewsResponse(response: Response<NewsResponse>):NewsResource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {  resultResponse ->
                return NewsResource.Success(resultResponse)

            }
        }
        return NewsResource.Erorr(null,response.message())
    }

    private fun handelSearchResponse(response: Response<NewsResponse>):NewsResource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {  resultResponse ->
                return NewsResource.Success(resultResponse)

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
}