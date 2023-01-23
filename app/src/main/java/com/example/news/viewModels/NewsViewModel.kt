package com.example.news.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val page=1

    fun getNews(countryCode:String)=viewModelScope.launch {
        news.postValue(NewsResource.Loading())
        val response=repository.getNews(countryCode,page)
        news.postValue(handelNewsResponse(response))
    }

    private fun handelNewsResponse(response: Response<NewsResponse>):NewsResource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {  resultResponse ->
                return NewsResource.Success(resultResponse)

            }
        }
        return NewsResource.Erorr(response.message())
    }
}