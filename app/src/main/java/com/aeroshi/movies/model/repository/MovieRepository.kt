package com.aeroshi.movies.model.repository

import com.aeroshi.movies.model.NetworkAPI
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieRepository {


    fun doMovies(apiKey: String, offset: Int): Single<String> {
        return NetworkAPI.getMovieService()
            .getMovies(apiKey, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}