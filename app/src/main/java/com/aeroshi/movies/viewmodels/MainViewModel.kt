package com.aeroshi.movies.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeroshi.movies.data.entitys.Result

class MainViewModel : ViewModel() {
    val mSelectedMovie = MutableLiveData<Result>()
}