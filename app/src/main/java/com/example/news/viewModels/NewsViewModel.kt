package com.example.news.viewModels

import androidx.lifecycle.ViewModel
import com.example.news.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: Repository):ViewModel() {
}