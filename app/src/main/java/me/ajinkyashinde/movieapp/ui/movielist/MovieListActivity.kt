package me.ajinkyashinde.movieapp.ui.movielist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.R
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ActivityMovieListBinding
import me.ajinkyashinde.movieapp.ui.moviedetails.MovieDetailsActivity
import me.ajinkyashinde.movieapp.ui.searchmovie.SearchMovieActivity
import me.ajinkyashinde.movieapp.utlis.AppConstant.MOVIE_ID_KEY
import me.ajinkyashinde.movieapp.utlis.Helper.isInternetAvailable
import me.ajinkyashinde.movieapp.utlis.Helper.showInternetNotFoundDialog
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
        setupObserver()
        setupUI()
    }

    private fun setupViewModel() {
        movieListViewModel = ViewModelProvider(this)[MovieListViewModel::class.java]
        if (isInternetAvailable(this)) movieListViewModel.fetchDiscoverMovieList()
        else {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setIcon(R.drawable.baseline_wifi_off_24)
                .setCancelable(false)
                .setPositiveButton("Try Again") { dialog, _ ->
                    if (isInternetAvailable(this)) {
                        movieListViewModel.fetchDiscoverMovieList()
                    } else {
                        showInternetNotFoundDialog(this)
                    }
                }
                .create()
            alertDialog.show()
        }
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchBar -> {
                val intent = Intent(this@MovieListActivity, SearchMovieActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {

        lifecycleScope.launch {
            movieListViewModel.fetchDiscoverMovieList().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            } else if (loadState.refresh is LoadState.NotLoading) {
                binding.progressBar.visibility = View.GONE
                if (adapter.itemCount > 0) {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                    binding.emptyView.text = "No data found"
                }
            } else if (loadState.refresh is LoadState.Error) {
                binding.progressBar.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
                binding.emptyView.text = "Error: Failed to fetch data"
            } else if (loadState.append.endOfPaginationReached) {
                binding.progressBar.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
                Toast.makeText(this, "No more pages to load", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
