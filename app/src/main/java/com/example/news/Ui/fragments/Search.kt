package com.example.news.Ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.util.Constant
import com.example.news.util.Constant.Companion.QUERY_PAGE_SIZE
import com.example.news.util.Constant.Companion.SEARCH_DELAY_TIME
import com.example.news.util.Constant.Companion.SEARCH_FRAGMENT_ID
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
                        val totalPages=searchResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.search_page == totalPages
                        if (isLastPage)
                        {
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }                    }
                }
                is NewsResource.Erorr -> {
                    hideShimmer()
                    response.message?.let { message ->
                        Toast.makeText(activity,"$message", Toast.LENGTH_LONG).show()
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

    var isLoading=false
    var isScrolling =false
    var isLastPage=false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val fristVisableItemPostion=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndIsNotAtLastPage= !isLoading && !isLastPage
            val isAtLastItem = fristVisableItemPostion + visibleItemCount >= totalItemCount
            val isNotAtBeginning =fristVisableItemPostion >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndIsNotAtLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                newsViewModel.searchNews(binding.etSearch.text.toString())
                isScrolling=false
            }

        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter(SEARCH_FRAGMENT_ID)
        binding.rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@Search.scrollListener)
        }
    }

    private fun hideShimmer(){
        isScrolling=false
        binding.rvSearchNews.setVisibility(View.VISIBLE)
        binding.rvSearchShimmer.setVisibility(View.GONE)
        binding.rvSearchShimmer.stopShimmerAnimation()
    }
    private fun showShimmer(){
        isScrolling=true
        binding.rvSearchNews.setVisibility(View.GONE)
        binding.rvSearchShimmer.setVisibility(View.VISIBLE)
        binding.rvSearchShimmer.startShimmerAnimation()
    }
}