package com.aeroshi.movies.viewmodels


import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeroshi.movies.data.AppDatabase
import com.aeroshi.movies.data.MoviesRepository
import com.aeroshi.movies.data.entitys.Result
import com.aeroshi.movies.model.repository.MovieRepository
import com.aeroshi.movies.util.BaseSchedulerProvider
import com.aeroshi.movies.util.Constants.Companion.KEY
import com.aeroshi.movies.util.ErrorType
import com.aeroshi.movies.util.Executors.Companion.ioThread
import com.aeroshi.movies.util.MoviesUtil.Companion.moviesJsonParser
import com.aeroshi.movies.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class HomeViewModel(
    private val mRepository: MovieRepository = MovieRepository(),
    private val mScheduler: BaseSchedulerProvider = SchedulerProvider()
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val mCompositeDisposable = CompositeDisposable()

    val mMoviesResult = MutableLiveData<Pair<ArrayList<Result>?, ErrorType>>()
    val mLoading = MutableLiveData(true)

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

    fun clearDisposables() = mCompositeDisposable.clear()


    fun doMovies(context: Context, items: Int = 20) {
        mLoading.postValue(true)
        mCompositeDisposable.add(
            mRepository
                .doMovies(KEY, items)
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui())
                .subscribeBy(
                    onSuccess = { jsonResult ->
                        try {
                            val movies = moviesJsonParser(jsonResult)
                            saveMoviesOnDb(context, movies)
                            mMoviesResult.value = Pair(movies, ErrorType.NONE)
                        } catch (exception: Exception) {
                            mLoading.value = false
                            exception.stackTrace
                        }
                    },
                    onError = {
                        mLoading.value = false
                        mMoviesResult.value = Pair(null, ErrorType.NETWORK)
                        Log.e(TAG, "Error on get movies: ${it.localizedMessage}")
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun saveMoviesOnDb(context: Context, movies: ArrayList<Result>) {
        ioThread {
            val configManagerRepository =
                MoviesRepository.getInstance(AppDatabase.getInstance(context).MoviesDao())

            configManagerRepository.insertMovies(movies)

        }
    }

}