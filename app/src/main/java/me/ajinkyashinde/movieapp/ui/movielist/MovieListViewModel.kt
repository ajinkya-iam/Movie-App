package me.ajinkyashinde.movieapp.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.model.MovieResponse
import me.ajinkyashinde.movieapp.data.repository.MainRepository
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.utlis.AppConstant.API_KEY
import me.ajinkyashinde.movieapp.utlis.AppConstant.DEFAULT_PAGE_INDEX
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    fun fetchDiscoverMovieList(): Flow<PagingData<MovieDetails>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(mainRepository) }
        ).flow.cachedIn(viewModelScope)
    }

}