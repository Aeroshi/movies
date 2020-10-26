package com.aeroshi.movies.views.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aeroshi.movies.databinding.FragmentAdapterBinding
import com.aeroshi.movies.viewmodels.HomeViewModel
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.aeroshi.movies.views.adapters.ViewPagerAdapter


class AdapterFragment : Fragment() {

    companion object {
        private const val TAG = "AdapterFragment"
    }

    private lateinit var mMainActivity: MainActivity

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mBinding: FragmentAdapterBinding


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
            requireActivity().supportFragmentManager.fragments.forEach { fragment ->
                if (fragment is FavoritesFragment) {
                    fragment.getFavoritesMovies()
                }
        }
    }

    private fun isMainViewModelInitialized() = ::mMainViewModel.isInitialized

    private fun initializeMainViewModel() {
        mMainViewModel = ViewModelProvider(mMainActivity).get(MainViewModel::class.java)
    }


    private fun isBindingInitialized() = ::mBinding.isInitialized

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = FragmentAdapterBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        setupViewPager()
    }


    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager,
            mMainActivity.applicationContext
        )
        mBinding.viewPager.adapter = adapter
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }


}