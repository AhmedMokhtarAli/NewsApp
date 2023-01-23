package com.example.news.Ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentNewsBinding
import com.example.news.util.NewsResource
import com.example.news.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class News : Fragment() {
    private lateinit var binding:FragmentNewsBinding
    private val newsViewModel by viewModels<NewsViewModel>()
    lateinit var newsAdapter: NewsAdapter
    val TAG="News TAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNewsBinding.inflate(layoutInflater)
        setupRecyclerView()

        newsViewModel.news.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is NewsResource.Success -> {
                    hideProgressPar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is NewsResource.Erorr -> {
                    hideProgressPar()
                    response.message?.let { message ->
                        Log.e(TAG,"error is $message")
                    }
                }
                is NewsResource.Loading ->{
                    showProgressPar()
                }
            }
        })

        return binding.root
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }

    private fun hideProgressPar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressPar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
    }
}