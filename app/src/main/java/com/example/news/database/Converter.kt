package com.example.news.database
import androidx.room.TypeConverter
import com.example.news.Source
class Converter {

    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}