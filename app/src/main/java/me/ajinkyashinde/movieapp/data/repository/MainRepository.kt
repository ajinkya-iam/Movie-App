package me.ajinkyashinde.movieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.ajinkyashinde.movieapp.data.api.NetworkService
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.model.MovieDetailsResponse
import me.ajinkyashinde.movieapp.data.model.MovieResponse
import me.ajinkyashinde.movieapp.ui.movielist.MoviePagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val networkService: NetworkService) {

//    fun getDiscoverMovieList(apiKey: String, page: Int): Flow<List<MovieDetails>> {
//        return flow {
//            emit(networkService.getDiscoverMovie(apiKey, page))
//        }.map {
//            it.results
//        }
//    }

    suspend fun getDiscoverMovieList(apiKey: String, page: Int): MovieResponse {
        return networkService.getDiscoverMovie(apiKey, page)
    }

    fun getMovieDetails(movieId: String, apiKey: String): Flow<MovieDetailsResponse> {
        return flow {
            emit(networkService.getMovieDetails(movieId, apiKey))
        }.map {
            it
        }
    }

    fun getSearchMovieList(query: String, apiKey: String): Flow<List<MovieDetails>> {
        return flow {
            emit(networkService.getSearchMovieList(query, apiKey))
        }.map {
            it.results
        }
    }
}