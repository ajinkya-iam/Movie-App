package me.ajinkyashinde.movieapp.data.api

import me.ajinkyashinde.movieapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("api_key") api_key: String, @Query("page") page: Int
    ): MovieResponse

}