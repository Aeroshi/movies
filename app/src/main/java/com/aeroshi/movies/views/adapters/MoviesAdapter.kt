package com.aeroshi.movies.views.adapters

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aeroshi.movies.R
import com.aeroshi.movies.data.Result
import com.aeroshi.movies.viewmodels.MainViewModel
import com.aeroshi.movies.views.MainActivity
import com.aeroshi.movies.views.adapters.viewHolders.MovieViewHolder
import com.squareup.picasso.Picasso

class MoviesAdapter(
    private val context: Context,
    private val mMainViewModel: MainViewModel,
    private val activity: MainActivity
) :
    RecyclerView.Adapter<MovieViewHolder>(), ActionMode.Callback {

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
                selectedMovies.forEach {
                    if (mMainViewModel.mFavoriteMovies.value!!.contains(it))
                        mMainViewModel.mFavoriteMovies.value!!.remove(it)
                    else
                        mMainViewModel.mFavoriteMovies.value!!.add(it)
                }
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
                .load(movies[position].multimedia.src)
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
                    .navigate(R.id.navigate_action_loginFragment_to_movieDetailsFragment)
            }
        }
    }

    fun update(movies: ArrayList<Result>) {
        this.movies.addAll(movies)
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

