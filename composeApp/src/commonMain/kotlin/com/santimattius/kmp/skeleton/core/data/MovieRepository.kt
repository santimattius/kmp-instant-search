package com.santimattius.kmp.skeleton.core.data

import com.santimattius.kmp.skeleton.core.data.datasources.RemoteMoviesDataSource
import com.santimattius.kmp.skeleton.core.data.models.asMovies
import com.santimattius.kmp.skeleton.core.domain.Movie
import com.santimattius.kmp.skeleton.core.data.models.Movie as MovieDto

class MovieRepository(
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
) {

    suspend fun getMovies(): List<Movie> {
        // TODO: add error handling
        val result: List<MovieDto> = remoteMoviesDataSource.getMovies().getOrThrow()
        return result.asMovies()
    }
}
