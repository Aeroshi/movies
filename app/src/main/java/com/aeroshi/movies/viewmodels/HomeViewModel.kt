package com.aeroshi.movies.viewmodels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeroshi.movies.data.Movies
import com.aeroshi.movies.data.Result
import com.aeroshi.movies.model.repository.MovieReposiyory
import com.aeroshi.movies.util.ErrorType
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val mRepository: MovieReposiyory = MovieReposiyory()
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val mCompositeDisposable = CompositeDisposable()

    val mMoviesResult = MutableLiveData<Pair<ArrayList<Result>?, ErrorType>>()
    val mLoading = MutableLiveData(true)
    val mItens = MutableLiveData<Int>().apply { value = 20 }


    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

    fun clearDisposables() = mCompositeDisposable.clear()


    fun doMovies() {
        mLoading.postValue(true)
        mCompositeDisposable.add(
            mRepository
                .getMovies("ZpiYxUtmBL38osoMCsnJAVTcdnBF13QD", mItens.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { jsonResult ->
                        try {
                            val gson = Gson()
                            val movies = gson.fromJson(jsonResult, Movies::class.java)
                            mMoviesResult.value = Pair(movies.results, ErrorType.NONE)
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

}