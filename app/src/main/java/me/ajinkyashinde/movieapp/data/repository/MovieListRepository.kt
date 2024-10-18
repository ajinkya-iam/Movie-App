package me.ajinkyashinde.movieapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.ajinkyashinde.movieapp.data.api.NetworkService
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRepository @Inject constructor(private val networkService: NetworkService) {
    fun getDiscoverMovieList(apiKey: String, page: Int): Flow<List<MovieDetails>> {
        return flow {
            emit(networkService.getDiscoverMovie(apiKey, page))
        }.map {
            it.results
        }
    }
}