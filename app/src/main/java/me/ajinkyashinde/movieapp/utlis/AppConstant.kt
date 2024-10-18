package me.ajinkyashinde.movieapp.utlis

import me.ajinkyashinde.movieapp.BuildConfig

object AppConstant {
    const val API_KEY = BuildConfig.API_KEY
    const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"
    const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780"
    const val DEFAULT_PAGE_INDEX = 1
    const val DEBOUNCE_TIMEOUT = 300L
    const val MIN_SEARCH_CHAR = 3

    const val MOVIE_ID_KEY = "movieId"
}