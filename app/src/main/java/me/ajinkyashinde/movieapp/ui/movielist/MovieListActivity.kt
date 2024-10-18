package me.ajinkyashinde.movieapp.ui.movielist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ActivityMovieListBinding
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.ui.moviedetails.MovieDetailsActivity
import me.ajinkyashinde.movieapp.utlis.AppConstant.MOVIE_ID_KEY
import javax.inject.Inject

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private lateinit var movieListViewModel: MovieListViewModel

    @Inject
    lateinit var adapter: MovieListAdapter

    private lateinit var binding: ActivityMovieListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupUI()
        setupObserver()
    }

    private fun setupViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setClickListener(object : MovieListAdapter.ItemClickListener {
            override fun onClickItem(view: View?, position: Int, movieDetails: MovieDetails?) {
                val intent = Intent(
                    this@MovieListActivity,
                    MovieDetailsActivity::class.java
                )
                intent.putExtra(MOVIE_ID_KEY, movieDetails?.id.toString())
                startActivity(intent)
            }
        })
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MovieListActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList: List<MovieDetails>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }
}
