package me.ajinkyashinde.movieapp.ui.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.ajinkyashinde.movieapp.data.model.MovieDetailsResponse
import me.ajinkyashinde.movieapp.data.repository.MainRepository
import me.ajinkyashinde.movieapp.ui.base.UiState
import me.ajinkyashinde.movieapp.utlis.AppConstant.API_KEY
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<MovieDetailsResponse>>(UiState.Loading)

    val uiState: StateFlow<UiState<MovieDetailsResponse>> = _uiState

    fun getMovieDetails(movieId: String) {
        viewModelScope.launch {
            mainRepository.getMovieDetails(movieId = movieId, apiKey = API_KEY)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}