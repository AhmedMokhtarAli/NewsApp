package com.example.news.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converter::class)
abstract class NewsDataBase :RoomDatabase() {
    abstract fun newsDao():Dao
}