package com.aeroshi.movies.views.adapters.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aeroshi.movies.R

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageViewMovie: ImageView = view.findViewById(R.id.imageView_movie)
    val imageViewTitleMovie: TextView = view.findViewById(R.id.textView_title_movie)

}