package me.ajinkyashinde.movieapp.data.api

import me.ajinkyashinde.movieapp.data.model.MovieDetailsResponse
import me.ajinkyashinde.movieapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("api_key") api_key: String, @Query("page") page: Int
    ): MovieResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String
    ): MovieDetailsResponse

    @GET("search/movie")
    suspend fun getSearchMovieList(
        @Query("query") query: String,
        @Query("api_key") api_key: String
    ): MovieResponse
}