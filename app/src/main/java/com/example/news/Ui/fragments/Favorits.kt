package com.example.news.Ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentFavoritsBinding
import com.example.news.util.Constant
import com.example.news.viewModels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Favorits : Fragment() {
    private lateinit var binding:FragmentFavoritsBinding
    private val viewModel:NewsViewModel by viewModels()
    private lateinit var favAdapter:NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoritsBinding.inflate(inflater)
        setupRecyclerView()

        viewModel.getFavoritArticles().observe(viewLifecycleOwner, Observer { articles ->
            favAdapter.differ.submitList(articles)
        })

        val itemTouchHelperCallback=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val postion=viewHolder.adapterPosition
                val article =favAdapter.differ.currentList[postion]
                viewModel.deletArticle(article)

                Snackbar.make(view!!,"item successfully deleted ",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.addToFavorit(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        return binding.root
    }
    private fun setupRecyclerView(){
        favAdapter = NewsAdapter(Constant.FAVORIT_FRAGMENT_ID)
        binding.rvSavedNews.apply {
            adapter=favAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }

}