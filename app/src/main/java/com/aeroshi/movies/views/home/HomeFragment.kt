package com.aeroshi.movies.views.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aeroshi.movies.R
import com.aeroshi.movies.data.AppDatabase
import com.aeroshi.movies.data.MoviesRepository
import com.aeroshi.movies.data.entitys.Result
import com.aeroshi.movies.databinding.FragmentHomeBinding
import com.aeroshi.movies.util.AdapterUtil
import com.aeroshi.movies.util.ErrorType
import com.aeroshi.movies.util.Executors.Companion.ioThread
import com.aeroshi.movies.util.enuns.AdapterType
import com.aeroshi.movies.viewmodels.HomeViewModel
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.aeroshi.movies.views.adapters.MoviesAdapter
import com.onurkaganaldemir.ktoastlib.KToast


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private lateinit var mMainActivity: MainActivity

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mViewModel: HomeViewModel

    private lateinit var mBinding: FragmentHomeBinding

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

        if (!isViewModelInitialized())
            initializeViewModel()

        if (!isBindingInitialized())
            initializeBinding(inflater, container)

        return mBinding.root
    }

    override fun onRefresh() {
        mBinding.swipeRefresh.isRefreshing = false
        mAdapter.movies.clear()

        ioThread {
            mMainActivity.applicationContext.let { context ->

                val configManagerRepository = MoviesRepository.getInstance(
                    AppDatabase.getInstance(context).MoviesDao()
                )
                configManagerRepository.deleteAll()

                mViewModel.doMovies(context)

            }
        }

    }

    override fun onStop() {
        super.onStop()
        mViewModel.clearDisposables()
    }

    override fun onResume() {
        super.onResume()
        if (!mViewModel.mMoviesResult.hasObservers())
            addViewModelObservers()
    }

    private fun isMainViewModelInitialized() = ::mMainViewModel.isInitialized

    private fun initializeMainViewModel() {
        mMainViewModel = ViewModelProvider(mMainActivity).get(MainViewModel::class.java)
    }

    private fun isViewModelInitialized() = ::mViewModel.isInitialized

    private fun initializeViewModel() {
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        addViewModelObservers()
    }

    private fun addViewModelObservers() {
        mViewModel.mMoviesResult.observe(viewLifecycleOwner, observeMoviesResult())
    }

    private fun isBindingInitialized() = ::mBinding.isInitialized

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        mBinding.swipeRefresh.setOnRefreshListener(this)
        mBinding.viewModel = mViewModel
        setAdapter()

        ioThread {
            mMainActivity.applicationContext.let { context ->

                val configManagerRepository = MoviesRepository.getInstance(
                    AppDatabase.getInstance(context).MoviesDao()
                )
                val movies = configManagerRepository.getMovies() as ArrayList<Result>
                if (movies.isNullOrEmpty()) {
                    mViewModel.doMovies(context)
                } else {
                    mViewModel.mMoviesResult.postValue(Pair(movies, ErrorType.NONE))
                }
            }
        }
        setupClickEvents()
    }


    private fun observeMoviesResult(): Observer<Pair<ArrayList<Result>?, ErrorType>> {
        return Observer { resultPair ->
            if (resultPair.second == ErrorType.NONE) {
                mViewModel.mLoading.value = false
                mAdapter.update(resultPair.first!!)
            } else {
                KToast.errorToast(
                    mMainActivity,
                    getString(R.string.error_movies),
                    Gravity.CENTER,
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    private fun setupClickEvents() {
        //TODO Implementar o carregar mais filmes
        mBinding.buttonMore.setOnClickListener {

        }
    }

    private fun setAdapter() {
        val mNoOfColumns: Int = AdapterUtil.calculateNoOfColumns(
            mMainActivity.applicationContext,
            200F
        )
        mAdapter = MoviesAdapter(mMainViewModel, mMainActivity, AdapterType.HOME)
        mGridLayoutManager = GridLayoutManager(mMainActivity.applicationContext, mNoOfColumns)
        mBinding.recyclerViewMovies.layoutManager = mGridLayoutManager
        mBinding.recyclerViewMovies.adapter = mAdapter
    }


}