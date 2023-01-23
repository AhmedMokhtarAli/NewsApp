package com.example.news.util

sealed class NewsResource<T>(
    data:T?=null,
    message:String?=null
){
    class Success<T>(data: T):NewsResource<T>(data)
    class Erorr<T>(message: String,data: T?= null ):NewsResource<T>(data,message)
    class Loading<T>:NewsResource<T>()
}