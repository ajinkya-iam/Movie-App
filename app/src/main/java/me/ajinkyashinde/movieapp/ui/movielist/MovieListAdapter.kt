package me.ajinkyashinde.movieapp.ui.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ItemDiscoverMovieBinding
import me.ajinkyashinde.movieapp.utlis.AppConstant.BASE_POSTER_PATH

class MovieListAdapter(
    private val movieList: ArrayList<MovieDetails>
) : RecyclerView.Adapter<MovieListAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: ItemDiscoverMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieDetails: MovieDetails) {
            binding.tvMovieName.text = movieDetails.originalTitle
            binding.tvMovieDetails.text = movieDetails.overview
            binding.tvMovieDate.text = movieDetails.releaseDate
            Glide.with(binding.ivImage.context)
                .load(BASE_POSTER_PATH + movieDetails.posterPath)
                .into(binding.ivImage)
            itemView.setOnClickListener {

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

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(movieList[position])

    fun addData(list: List<MovieDetails>) {
        movieList.addAll(list)
    }

}