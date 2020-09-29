package com.aeroshi.movies

import android.app.Application
import com.aeroshi.movies.data.AppDatabase
import com.aeroshi.movies.data.MoviesRepository
import com.aeroshi.movies.util.Executors.Companion.ioThread

class MoviesApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MoviesApplication? = null

        fun applicationContext(): MoviesApplication {
            return instance as MoviesApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        inicializedBD()
    }

    private fun inicializedBD() {
        ioThread {
            val configManagerRepository = MoviesRepository.getInstance(
                AppDatabase.getInstance(applicationContext).MoviesDao()
            )
            configManagerRepository.getMovies()
        }
    }
}