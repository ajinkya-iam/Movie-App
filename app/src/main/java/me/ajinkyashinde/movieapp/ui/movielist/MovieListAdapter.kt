package me.ajinkyashinde.movieapp.ui.movielist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ItemDiscoverMovieBinding
import me.ajinkyashinde.movieapp.utlis.AppConstant.BASE_POSTER_PATH

class MovieListAdapter : PagingDataAdapter<MovieDetails, MovieListAdapter.DataViewHolder>(MOVIE_COMPARATOR) {

    interface ItemClickListener {
        fun onClickItem(view: View?, position: Int, movieDetails: MovieDetails?)
    }

    companion object {
        var mClickListener: ItemClickListener? = null

        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieDetails>() {
            override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean =
                oldItem == newItem
        }
    }


    class DataViewHolder(private val binding: ItemDiscoverMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movieDetails: MovieDetails?) {
            movieDetails?.run {
                binding.tvMovieName.text = movieDetails.originalTitle
                binding.tvMovieDetails.text = movieDetails.overview
                binding.tvMovieDate.text = "Release Date : ${movieDetails.releaseDate}"
                Glide.with(binding.ivImage.context)
                    .load(BASE_POSTER_PATH + movieDetails.posterPath)
                    .into(binding.ivImage)
                itemView.setOnClickListener {
                    it.startAnimation(AlphaAnimation(3f, 0.2f))
                    mClickListener?.onClickItem(it, position, movieDetails = movieDetails)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            ItemDiscoverMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(getItem(position))


    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

}