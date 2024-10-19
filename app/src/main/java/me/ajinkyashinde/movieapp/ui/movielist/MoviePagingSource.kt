package me.ajinkyashinde.movieapp.ui.movielist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.ajinkyashinde.movieapp.data.model.MovieDetails
import me.ajinkyashinde.movieapp.data.model.MovieResponse
import me.ajinkyashinde.movieapp.data.repository.MainRepository
import me.ajinkyashinde.movieapp.utlis.AppConstant.API_KEY
import me.ajinkyashinde.movieapp.utlis.AppConstant.DEFAULT_PAGE_INDEX

class MoviePagingSource(
    private val mainRepository: MainRepository
) : PagingSource<Int, MovieDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        return try {
            val page = params.key ?: DEFAULT_PAGE_INDEX
            val response = mainRepository.getDiscoverMovieList(apiKey = API_KEY, page =page)

            LoadResult.Page(
                data = response.results, // Movies list from the response
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDetails>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
