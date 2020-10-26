package com.aeroshi.movies.views.adapters

import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aeroshi.movies.AppPreferences
import com.aeroshi.movies.R
import com.aeroshi.movies.data.entitys.Result
import com.aeroshi.movies.util.enuns.AdapterType
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.aeroshi.movies.views.adapters.viewHolders.MovieViewHolder
import com.google.gson.Gson
import com.onurkaganaldemir.ktoastlib.KToast
import com.squareup.picasso.Picasso


class MoviesAdapter(
    private val mMainViewModel: MainViewModel,
    private val activity: MainActivity,
    private val adapterType: AdapterType,
) :
    RecyclerView.Adapter<MovieViewHolder>(), ActionMode.Callback {

    private val context = activity.applicationContext

    private val inflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    var movies: ArrayList<Result> = arrayListOf()
    private var multiSelect = false
    private val selectedMovies: ArrayList<Result> = arrayListOf()

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater: MenuInflater = mode.menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val movies = AppPreferences.getFavoritesMovies(context)
                selectedMovies.forEach {
                    if (movies.contains(it)) {
                        movies.remove(it)
                    } else {
                        movies.add(it)
                    }
                }
                AppPreferences.setFavoritesMovies(context, Gson().toJson(movies))
                if (adapterType == AdapterType.FAVORITE) {
                    this.movies = movies
                    notifyDataSetChanged()
                }
                KToast.infoToast(
                    activity, context.getString(R.string.favoriteUpdated),
                    Gravity.BOTTOM,
                    Toast.LENGTH_LONG
                )
            }
        }
        mode.finish()
        return true
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        multiSelect = false
        selectedMovies.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(inflater.inflate(R.layout.card_movies, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movies[position]
        try {
            Picasso.get()
                .load(movies[position].multimedia?.src)
                .into(holder.imageViewMovie)
            holder.imageViewMovie.scaleType = ImageView.ScaleType.CENTER_CROP
        } catch (exception: Exception) {
            Log.e("MoviesAdapter", "Error on set image movie: ${exception.localizedMessage}")
            exception.stackTrace
        }
        holder.imageViewTitleMovie.text = currentMovie.display_title

        if (selectedMovies.contains(currentMovie)) {
            holder.itemView.alpha = 0.3f
        } else {
            holder.itemView.alpha = 1.0f
        }

        holder.itemView.setOnLongClickListener {
            if (!multiSelect) {
                multiSelect = true
                activity.setActionMode(this)
                selectItem(holder, movies[holder.adapterPosition])
                true
            } else
                false
        }

        holder.itemView.setOnClickListener {
            if (multiSelect)
                selectItem(holder, currentMovie)
            else {
                mMainViewModel.mSelectedMovie.value = movies[position]
                Navigation.findNavController(it)
                    .navigate(R.id.navigate_adapterFragment_to_movieDetailsFragment)
            }
        }
    }

    fun update(movies: ArrayList<Result>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun create(movies: ArrayList<Result>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    private fun selectItem(holder: MovieViewHolder, movie: Result) {
        if (selectedMovies.contains(movie)) {
            selectedMovies.remove(movie)
            holder.itemView.alpha = 1.0f
        } else {
            selectedMovies.add(movie)
            holder.itemView.alpha = 0.3f
        }
    }

}

