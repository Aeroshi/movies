package com.aeroshi.movies.util

import android.content.Context

class AdapterUtil {
    companion object {
        fun calculateNoOfColumns(
            context: Context,
            columnWidthDp: Float
        ): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }
}