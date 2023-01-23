package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.news.api.NewsAPIHelper
import com.example.news.api.NewsAPIServices
import com.example.news.api.NewsHelperImp
import com.example.news.database.NewsDataBase
import com.example.news.util.Constant.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Singleton
    @Provides
    fun provideOhHttpClient():OkHttpClient{
        val loggingInterceptor=HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit=Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit) = retrofit.create(NewsAPIServices::class.java)

    @Provides
    @Singleton
    fun provideNewsApiHelper(newsHelperImp: NewsHelperImp): NewsAPIHelper = newsHelperImp

    @Singleton
    @Provides
    fun provideDao(newsDataBase: NewsDataBase)=newsDataBase.newsDao()

    fun provideNewsDataBase(@ApplicationContext context: Context):NewsDataBase{
        return Room.databaseBuilder(context,
            NewsDataBase::class.java,
            "NewsDataBase"
            ).build()
    }
}