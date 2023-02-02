package com.example.news.Ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentNewsBinding
import com.example.news.util.Constant.Companion.NEWS_FRAGMENT_ID
import com.example.news.util.Constant.Companion.QUERY_PAGE_SIZE
import com.example.news.util.NewsResource
import com.example.news.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class News : Fragment() {
    private lateinit var binding:FragmentNewsBinding
    private val newsViewModel:NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter
    val TAG="newsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentNewsBinding.inflate(layoutInflater)
        setupRecyclerView()


        newsViewModel.news.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is NewsResource.Success -> {
                    hideShimmer()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.news_page == totalPages
                        if (isLastPage)
                        {
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is NewsResource.Erorr -> {
                    hideShimmer()
                    response.message?.let { message ->
                       Toast.makeText(activity,"$message",Toast.LENGTH_LONG).show()
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
        newsAdapter = NewsAdapter(NEWS_FRAGMENT_ID)
        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@News.scrollListener)
        }
    }
    var isLoading=false
    var isScrolling =false
    var isLastPage=false

    val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
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
                newsViewModel.getNews("us")
                isScrolling=false
            }

        }
    }

    private fun hideShimmer(){
        isLoading=false
        binding.rvBreakingNews.setVisibility(View.VISIBLE)
        binding.rvBreakingNewShimmer.setVisibility(View.GONE)
        binding.rvBreakingNewShimmer.stopShimmerAnimation()
    }
    private fun showShimmer(){
        isLoading=true
        binding.rvBreakingNews.setVisibility(View.GONE)
        binding.rvBreakingNewShimmer.setVisibility(View.VISIBLE)
        binding.rvBreakingNewShimmer.startShimmerAnimation()
    }

}