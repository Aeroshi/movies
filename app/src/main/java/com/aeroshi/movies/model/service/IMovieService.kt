package com.aeroshi.movies.model.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieService {

    @GET("svc/movies/v2/reviews/search.json")
    fun getMovies(@Query("api-key") apiKey: String, @Query("offset") offset: Int): Single<String>
}