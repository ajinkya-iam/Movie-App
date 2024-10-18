package me.ajinkyashinde.movieapp.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.repository.MainRepository
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.utlis.AppConstant.API_KEY
import me.ajinkyashinde.movieapp.utlis.AppConstant.DEFAULT_PAGE_INDEX
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<MovieDetails>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<MovieDetails>>> = _uiState

    fun fetchDiscoverMovieList(page: Int = DEFAULT_PAGE_INDEX) {
        viewModelScope.launch {
            mainRepository.getDiscoverMovieList(apiKey = API_KEY, page = page)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

}