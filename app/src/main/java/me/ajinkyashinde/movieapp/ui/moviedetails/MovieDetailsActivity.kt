package me.ajinkyashinde.movieapp.ui.moviedetails

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import me.ajinkyashinde.movieapp.utlis.Helper

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var binding: ActivityMovieDetailsBinding
    companion object{
        private const val TAG = "MovieDetailsActivity"
    }

    private var movieId: String = ""

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        if(intent.hasExtra(MOVIE_ID_KEY)) movieId = intent.getStringExtra(MOVIE_ID_KEY)!!
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        movieDetailsViewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        if (Helper.isInternetAvailable(this)) movieDetailsViewModel.getMovieDetails(movieId = movieId)
        else {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setIcon(R.drawable.baseline_wifi_off_24)
                .setCancelable(false)
                .setPositiveButton("Try Again") { dialog, _ ->
                    if (Helper.isInternetAvailable(this)) {
                        movieDetailsViewModel.getMovieDetails(movieId = movieId)
                    } else {
                        Helper.showInternetNotFoundDialog(this)
                    }
                }
                .create()
            alertDialog.show()
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailsViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            progressDialog.dismiss()
                            setupUI(it.data)
                        }
                        is UiState.Loading -> {
                            progressDialog.setTitle("Loading...")
                            progressDialog.show()
                        }
                        is UiState.Error -> {
                            progressDialog.dismiss()
                            val alertDialog = AlertDialog.Builder(this@MovieDetailsActivity)
                                .setTitle("Failed fetch data")
                                .setMessage(it.message)
                                .setCancelable(false)
                                .setPositiveButton("Close") { dialog, _ ->
                                    onBackPressed()
                                }
                                .create()
                            alertDialog.show()
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