package com.aeroshi.movies


import android.content.Context
import com.aeroshi.movies.data.entitys.Result
import com.aeroshi.movies.extensions.logDebug
import com.aeroshi.movies.extensions.logWarning
import com.aeroshi.movies.util.Constants.Companion.APP_PREFERENCES
import com.aeroshi.movies.util.Constants.Companion.MOVIES
import com.aeroshi.movies.util.MoviesUtil.Companion.moviesJsonParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


abstract class AppPreferences {

    companion object {

        private const val TAG = "AppPreferences"

        fun setFavoritesMovies(context: Context, json: String) {
            logDebug(TAG, "saving the movies json  on preferences")
            val prefs =
                context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

            prefs.edit().putString(MOVIES, json).apply()
        }


        fun getFavoritesMovies(context: Context): ArrayList<Result> {
            logDebug(TAG, "getting the movies json saved on preferences")
            return try {
                val prefs =
                    context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                val json = prefs.getString(MOVIES, "")
                if (!json.isNullOrBlank() && !json.isNullOrEmpty())
                    jsonToArray(json)
                else
                    arrayListOf()
            } catch (exception: Exception) {
                logWarning(
                    TAG,
                    "Error on find the movies : ${exception.message}"
                )
                arrayListOf()
            }
        }

        private fun jsonToArray(json: String): ArrayList<Result> {
            val typeToken = object : TypeToken<List<Result>>() {}.type
            return Gson().fromJson(json, typeToken)
        }


    }
}

