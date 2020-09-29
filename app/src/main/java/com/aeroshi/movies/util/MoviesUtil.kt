package com.aeroshi.movies.util

import com.aeroshi.movies.data.entitys.Movies
import com.aeroshi.movies.data.entitys.Result
import com.google.gson.Gson

class MoviesUtil {
    companion object{
        fun moviesJsonParser(json: String):ArrayList<Result>{
            val gson = Gson()
            val movies = gson.fromJson(json, Movies::class.java)
            return movies.results
        }

    }
}