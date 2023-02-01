package com.example.news.Ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.news.Article
import com.example.news.R
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.viewModels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    private val args by navArgs<ArticleFragmentArgs>()
    private lateinit var binding:FragmentArticleBinding
    private val viewModel:NewsViewModel by viewModels()
    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentArticleBinding.inflate(inflater)

        article=args.article

        binding.article=article
        binding.webView.apply {
            webViewClient= WebViewClient()
        }

        binding.fab.setOnClickListener{
            viewModel.addToFavorit(article)
            Snackbar.make(it,"article saved successfully",Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }
}