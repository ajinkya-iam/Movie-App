package me.ajinkyashinde.movieapp.ui.searchmovie

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ItemDiscoverMovieBinding
import me.ajinkyashinde.movieapp.utlis.AppConstant.BASE_POSTER_PATH

class SearchMovieListAdapter(
    private val movieList: ArrayList<MovieDetails>
) : RecyclerView.Adapter<SearchMovieListAdapter.DataViewHolder>() {

    interface ItemClickListener {
        fun onClickItem(view: View?, position: Int, movieDetails: MovieDetails?)
    }

    companion object {
        var mClickListener: ItemClickListener? = null
    }


    class DataViewHolder(private val binding: ItemDiscoverMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movieDetails: MovieDetails) {
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

    fun clearData(){
        movieList.clear()
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

}