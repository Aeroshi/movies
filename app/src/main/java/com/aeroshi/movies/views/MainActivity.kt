package com.aeroshi.movies.views

import android.os.Bundle
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.aeroshi.movies.R
import com.aeroshi.movies.databinding.ActivityMainBinding
import com.aeroshi.movies.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var mViewModel: MainViewModel

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mNavController: NavController

    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        setSupportActionBar(mBinding.myToolbar)
        mNavController = findNavController(R.id.fragment_main)
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph)
        setupActionBarWithNavController(mNavController, mAppBarConfiguration)

    }

    fun setActionMode(callback: ActionMode.Callback) {
        mBinding.myToolbar.startActionMode(callback)
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp(mAppBarConfiguration) || super.onSupportNavigateUp()
    }
}