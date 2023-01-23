package com.example.news.Ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.news.Article
import com.example.news.Ui.fragments.NewsDirections

@BindingAdapter("setImageResource")
fun ImageView.setImageResource(url:String){
    Glide.with(this).load(url).into(this)
}

@BindingAdapter("navToArticle")
fun View.OnClick(article: Article){
    this.setOnClickListener(View.OnClickListener { action_news_to_article(article,this) })
}

fun action_news_to_article(article: Article, view: View) {
    val action=NewsDirections.actionNewsToArticle()
    view.findNavController().navigate(action)
}
