package com.aeroshi.movies.views.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aeroshi.movies.R
import com.aeroshi.movies.views.home.FavoritesFragment
import com.aeroshi.movies.views.home.HomeFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            HomeFragment()
        else
            FavoritesFragment()

    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0)
            context.getString(R.string.home)
        else
            context.getString(R.string.favorite)
    }

}