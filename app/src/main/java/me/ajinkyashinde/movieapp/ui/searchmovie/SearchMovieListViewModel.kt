package me.ajinkyashinde.movieapp.ui.searchmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.repository.MainRepository
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.utlis.AppConstant.API_KEY
import me.ajinkyashinde.movieapp.utlis.AppConstant.DEBOUNCE_TIMEOUT
import me.ajinkyashinde.movieapp.utlis.AppConstant.MIN_SEARCH_CHAR
import javax.inject.Inject

@HiltViewModel
class SearchMovieListViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val searchText = MutableStateFlow("")
    private val _uiState =
        MutableStateFlow<UiState<List<MovieDetails>>>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState<List<MovieDetails>>> = _uiState

    init {
        createNewsFlow()
    }

    private fun createNewsFlow() {
        viewModelScope.launch {
            searchText.debounce(DEBOUNCE_TIMEOUT)
                .filter {
                    if (it.isNotEmpty() && it.length >= MIN_SEARCH_CHAR) {
                        return@filter true
                    } else {
                        _uiState.value = UiState.Success(emptyList())
                        return@filter false
                    }
                }.distinctUntilChanged()
                .flatMapLatest {
                    _uiState.value = UiState.Loading
                    return@flatMapLatest mainRepository.getSearchMovieList(it, API_KEY)
                        .catch { e ->
                            _uiState.value = UiState.Error(e.toString())
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun onQuerySearch(query: String) {
        searchText.value = query
    }
}