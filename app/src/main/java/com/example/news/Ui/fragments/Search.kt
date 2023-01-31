package com.example.news.Ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.util.Constant.Companion.SEARCH_DELAY_TIME
import com.example.news.util.NewsResource
import com.example.news.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class Search : Fragment() {

    private lateinit var binding:FragmentSearchBinding
    private val newsViewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter
    val TAG="newsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(layoutInflater)
        setupRecyclerView()

        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_DELAY_TIME)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is NewsResource.Success -> {
                    hideShimmer()
                    response.data?.let { searchResponse ->
                        newsAdapter.differ.submitList(searchResponse.articles)
                        Log.d(TAG,"size is ${searchResponse.articles.size}" )
                    }
                }
                is NewsResource.Erorr -> {
                    hideShimmer()
                    response.message?.let { message ->
                        Log.e(TAG,"error is $message")
                    }
                }
                is NewsResource.Loading ->{
                    showShimmer()
                    Log.d(TAG,"loading" )
                }
            }
        })

        return binding.root
    }


    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }

    private fun hideShimmer(){
        binding.rvSearchShimmer.setVisibility(View.GONE)
        binding.rvSearchShimmer.stopShimmerAnimation()
    }
    private fun showShimmer(){
        binding.rvSearchShimmer.setVisibility(View.VISIBLE)
        binding.rvSearchShimmer.startShimmerAnimation()
    }
}