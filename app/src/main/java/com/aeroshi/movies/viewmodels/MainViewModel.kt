package com.aeroshi.movies.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeroshi.movies.data.Result

class MainViewModel : ViewModel() {
    val mSelectedMovie = MutableLiveData<Result>()
    val mFavoriteMovies = MutableLiveData<ArrayList<Result>>().apply { value = arrayListOf() }
}