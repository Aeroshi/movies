package com.aeroshi.movies.data

import androidx.room.TypeConverter
import com.aeroshi.movies.data.entitys.Link
import com.aeroshi.movies.data.entitys.Multimedia
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun linkToJson(value: Link) = Gson().toJson(value)

    @TypeConverter
    fun jsonToLink(value: String) = Gson().fromJson(value, Link::class.java)

    @TypeConverter
    fun multimediaToJson(value: Multimedia) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMultimedia(value: String) = Gson().fromJson(value, Multimedia::class.java)

}
