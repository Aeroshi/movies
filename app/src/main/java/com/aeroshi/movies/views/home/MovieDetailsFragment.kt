package com.aeroshi.movies.views.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aeroshi.movies.AppPreferences
import com.aeroshi.movies.R
import com.aeroshi.movies.databinding.FragmentMovieDetailsBinding
import com.aeroshi.movies.extensions.navigate
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.google.gson.Gson
import com.onurkaganaldemir.ktoastlib.KToast
import com.squareup.picasso.Picasso

class MovieDetailsFragment : Fragment() {

    companion object {
        private const val TAG = "MovieDetailsFragment"
    }

    private lateinit var mMainActivity: MainActivity

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mBinding: FragmentMovieDetailsBinding


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


    private fun isMainViewModelInitialized() = ::mMainViewModel.isInitialized

    private fun initializeMainViewModel() {
        mMainViewModel = ViewModelProvider(mMainActivity).get(MainViewModel::class.java)
    }


    private fun isBindingInitialized() = ::mBinding.isInitialized

    private fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = this
        try {
            Picasso.get()
                .load(mMainViewModel.mSelectedMovie.value!!.multimedia?.src)
                .into(mBinding.imageViewCover)
            mBinding.imageViewCover.scaleType = ImageView.ScaleType.CENTER_CROP
            mBinding.textViewTitle.text = mMainViewModel.mSelectedMovie.value!!.display_title
            mBinding.textViewDirector.text = mMainViewModel.mSelectedMovie.value!!.byline
            mBinding.textViewStoryline.text = mMainViewModel.mSelectedMovie.value!!.summary_short
        } catch (exception: Exception) {
            navigate(R.id.navigate_movieDetailsFragment_to_adapterFragment)
            KToast.errorToast(
                mMainActivity,
                getString(R.string.error_details),
                Gravity.CENTER,
                Toast.LENGTH_SHORT
            )
            Log.e(TAG, "Error on set details movie: ${exception.localizedMessage}")
            exception.stackTrace
        }
        setupClickEvents()
    }

    private fun setupClickEvents() {

        mBinding.buttonSaveFavorite.setOnClickListener {

            mMainActivity.applicationContext.let { context ->

                val movies = AppPreferences.getFavoritesMovies(context)

                if (movies.contains(mMainViewModel.mSelectedMovie.value)) {
                    movies.remove(mMainViewModel.mSelectedMovie.value!!)
                    KToast.infoToast(
                        mMainActivity, context.getString(R.string.favoriteRemove), Gravity.BOTTOM,
                        Toast.LENGTH_LONG
                    )
                } else {
                    movies.add(mMainViewModel.mSelectedMovie.value!!)
                    KToast.infoToast(
                        mMainActivity,
                        context.getString(R.string.favoriteAdd),
                        Gravity.BOTTOM,
                        Toast.LENGTH_LONG
                    )
                }

                AppPreferences.setFavoritesMovies(context, Gson().toJson(movies))
            }
        }

        mBinding.buttonShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(mMainActivity)
                .setType("text/plain")
                .setChooserTitle("Share Movie")
                .setText(mMainViewModel.mSelectedMovie.value!!.link?.url)
                .startChooser();
        }

        mBinding.buttonReadMore.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(mMainViewModel.mSelectedMovie.value!!.link?.url)
            startActivity(openURL)
        }

    }

}