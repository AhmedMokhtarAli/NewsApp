package com.example.news.Ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.news.Article
import com.example.news.Ui.fragments.FavoritsDirections
import com.example.news.Ui.fragments.NewsDirections
import com.example.news.Ui.fragments.SearchDirections
import com.example.news.util.Constant.Companion.NEWS_FRAGMENT_ID
import com.example.news.util.Constant.Companion.SEARCH_FRAGMENT_ID

@BindingAdapter("setImageResource")
fun ImageView.setImageResource(url:String?){
    url?.let{ imageUrl ->
        Glide.with(this).load(imageUrl).into(this)
    }
}

@BindingAdapter("navToArticle","fragmentId")
fun View.OnClick(article: Article,fragmentId: Int){
    this.setOnClickListener(View.OnClickListener {

        navigateToArticle(article,fragmentId,this) }

    )
}

fun navigateToArticle(article: Article,fragmentId:Int ,view: View) {
    var action:NavDirections?=null
    if(fragmentId== NEWS_FRAGMENT_ID){
        action=NewsDirections.actionNewsToArticle(article)
        view.findNavController().navigate(action)
    }
    else if (fragmentId== SEARCH_FRAGMENT_ID){
        action= SearchDirections.actionSearchToArticle(article)
        view.findNavController().navigate(action)
    }
    else{
        action= FavoritsDirections.actionFavoritsToArticle(article)
        view.findNavController().navigate(action)
    }


}


