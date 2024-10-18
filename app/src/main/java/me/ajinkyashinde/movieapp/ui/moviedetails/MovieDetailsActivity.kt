package me.ajinkyashinde.movieapp.ui.moviedetails

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.R
import me.ajinkyashinde.movieapp.data.model.MovieDetailsResponse
import me.ajinkyashinde.movieapp.databinding.ActivityMovieDetailsBinding
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.ui.movielist.MovieListViewModel
import me.ajinkyashinde.movieapp.utlis.AppConstant
import me.ajinkyashinde.movieapp.utlis.AppConstant.MOVIE_ID_KEY

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var binding: ActivityMovieDetailsBinding
    companion object{
        private const val TAG = "MovieDetailsActivity"
    }

    private var movieId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.hasExtra(MOVIE_ID_KEY)) movieId = intent.getStringExtra(MOVIE_ID_KEY)!!
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        movieDetailsViewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        movieDetailsViewModel.getMovieDetails(movieId = movieId)
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailsViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
//                            binding.progressBar.visibility = View.GONE
                            setupUI(it.data)
                        }
                        is UiState.Loading -> {
//                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is UiState.Error -> {
                            //Handle Error
//                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MovieDetailsActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(movieDetailsResponse: MovieDetailsResponse) {
        binding.tvAdult.text = if(movieDetailsResponse.adult) "18+" else "14+"
        binding.tvMovieTitle.text = movieDetailsResponse.title
        binding.tvTagline.text = movieDetailsResponse.tagline
        binding.tvReleaseDate.text = "Release Date : ${movieDetailsResponse.releaseDate}"
        binding.tvRuntime.text = "Run Time : ${movieDetailsResponse.runtime} min"
        binding.tvOverview.text = movieDetailsResponse.overview
        Glide.with(binding.ivPosterImage.context)
            .load(AppConstant.BASE_BACKDROP_PATH + movieDetailsResponse.backdropPath)
            .into(binding.ivPosterImage)
    }

}