package me.ajinkyashinde.movieapp.ui.searchmovie

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.R
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.databinding.ActivitySearchMovieBinding
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.ui.moviedetails.MovieDetailsActivity
import me.ajinkyashinde.movieapp.utlis.AppConstant
import me.ajinkyashinde.movieapp.utlis.Helper.isInternetAvailable
import me.ajinkyashinde.movieapp.utlis.Helper.showInternetNotFoundDialog
import javax.inject.Inject

@AndroidEntryPoint
class SearchMovieActivity : AppCompatActivity() {

    private lateinit var searchMovieListViewModel: SearchMovieListViewModel

    @Inject
    lateinit var adapter: SearchMovieListAdapter

    private lateinit var binding: ActivitySearchMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupUI()
        setupQuery()
        setupObserver()
    }

    private fun setupViewModel() {
        searchMovieListViewModel = ViewModelProvider(this)[SearchMovieListViewModel::class.java]
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setClickListener(object : SearchMovieListAdapter.ItemClickListener {
            override fun onClickItem(view: View?, position: Int, movieDetails: MovieDetails?) {
                val intent = Intent(
                    this@SearchMovieActivity,
                    MovieDetailsActivity::class.java
                )
                intent.putExtra(AppConstant.MOVIE_ID_KEY, movieDetails?.id.toString())
                startActivity(intent)
            }
        })
    }

    private fun setupQuery() {

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (isInternetAvailable(this@SearchMovieActivity))
                        searchMovieListViewModel.onQuerySearch(newText)
                    else {
                        val alertDialog = AlertDialog.Builder(this@SearchMovieActivity)
                            .setTitle("No Internet Connection")
                            .setMessage("Please check your internet connection and try again.")
                            .setIcon(R.drawable.baseline_wifi_off_24)
                            .setCancelable(false)
                            .setPositiveButton("Try Again") { dialog, _ ->
                                if (isInternetAvailable(this@SearchMovieActivity)) {
                                    searchMovieListViewModel.onQuerySearch(newText)
                                } else {
                                    showInternetNotFoundDialog(this@SearchMovieActivity)
                                }
                            }
                            .create()
                        alertDialog.show()
                    }
                }
                return true
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchMovieListViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            if (it.data.isNotEmpty()) {
                                binding.progressBar.visibility = View.GONE
                                renderList(it.data)
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.emptyView.text = ""
                            } else {
                                renderList(it.data)
                                binding.emptyView.text = "Sorry, it looks like we couldn't find " +
                                        "details for the movie you're looking for. Please try searching" +
                                        " for another movie name."
                            }
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyView.text = ""
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.emptyView.text = "Error : ${it.message}"
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList: List<MovieDetails>) {
        adapter.clearData()
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }
}