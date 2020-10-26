package com.aeroshi.movies.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.aeroshi.movies.AppPreferences
import com.aeroshi.movies.databinding.FragmentFavoritesBinding
import com.aeroshi.movies.extensions.logDebug
import com.aeroshi.movies.util.AdapterUtil
import com.aeroshi.movies.util.enuns.AdapterType
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.aeroshi.movies.views.adapters.MoviesAdapter


class FavoritesFragment : Fragment() {

    companion object {
        private const val TAG = "FavoritesFragment"
    }

    private lateinit var mMainActivity: MainActivity

    private lateinit var mMainViewModel: MainViewModel


    private lateinit var mBinding: FragmentFavoritesBinding

    private lateinit var mAdapter: MoviesAdapter

    private lateinit var mGridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMainActivity = activity as MainActivity


        if (!isMainViewModelInitialized())
            initializeMainViewModel()

        if (!isBindingInitialized())
            initializeBinding(inflater, container)

        return mBinding.root
    }


    override fun onResume() {
        super.onResume()
        getFavoritesMovies()
    }


    private fun isMainViewModelInitialized() = ::mMainViewModel.isInitialized

    private fun initializeMainViewModel() {
        mMainViewModel = ViewModelProvider(mMainActivity).get(MainViewModel::class.java)
    }


    private fun isBindingInitialized() = ::mBinding.isInitialized

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        setAdapter()
    }

    fun getFavoritesMovies() {
        val favoritesMovies = AppPreferences.getFavoritesMovies(mMainActivity.applicationContext)
        mAdapter.create(favoritesMovies)
    }


    private fun setAdapter() {
        val mNoOfColumns: Int = AdapterUtil.calculateNoOfColumns(
            mMainActivity.applicationContext,
            200F
        )
        mAdapter = MoviesAdapter(mMainViewModel, mMainActivity, AdapterType.FAVORITE)
        mGridLayoutManager = GridLayoutManager(mMainActivity.applicationContext, mNoOfColumns)
        mBinding.recyclerViewMovies.layoutManager = mGridLayoutManager
        mBinding.recyclerViewMovies.adapter = mAdapter
    }


}