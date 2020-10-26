package com.aeroshi.movies.data

import com.aeroshi.movies.data.entitys.Result

class MoviesRepository private constructor(private val moviesDao: MoviesDao) {


    fun getMovies() = moviesDao.getMovies()

    fun insertMovies(movies: ArrayList<Result>) = moviesDao.insertData(movies)

    fun deleteAll() = moviesDao.deleteAll()

    companion object {

        @Volatile
        private var instance: MoviesRepository? = null

        fun getInstance(moviesDao: MoviesDao) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: MoviesRepository(
                            moviesDao
                        ).also { instance = it }
                }


    }
}