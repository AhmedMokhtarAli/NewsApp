package com.example.news.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.news.NewsApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class AppViewModel @Inject constructor (application: Application) : AndroidViewModel(application) {
    protected val context
    get()=getApplication<NewsApplication>()
}