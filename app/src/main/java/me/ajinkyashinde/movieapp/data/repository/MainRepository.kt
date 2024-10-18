package me.ajinkyashinde.movieapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.ajinkyashinde.movieapp.data.api.NetworkService
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.model.MovieDetailsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val networkService: NetworkService) {
    fun getDiscoverMovieList(apiKey: String, page: Int): Flow<List<MovieDetails>> {
        return flow {
            emit(networkService.getDiscoverMovie(apiKey, page))
        }.map {
            it.results
        }
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