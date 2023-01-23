package com.example.news.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.news.Article

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article:Article):Long

    @Query("select * from articles")
    fun getArticles():LiveData<List<Article>>

    @Delete
    suspend fun deletArticle(article: Article)
}