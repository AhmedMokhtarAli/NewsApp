package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.news.Article
import com.example.news.databinding.ItemArticleBinding

class NewsAdapter(val fragmentId:Int) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val differCallback=object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val articleItemBinding=ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleViewHolder(articleItemBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.bindArticle(article,fragmentId)
    }

    override fun getItemCount(): Int =differ.currentList.size

    class ArticleViewHolder(val articleItem:ItemArticleBinding): RecyclerView.ViewHolder(articleItem.root){
       fun bindArticle(article: Article,fragmentId:Int){
           articleItem.article=article
           articleItem.fragmentId=fragmentId
       }
    }
}