package com.santimattius.kmp.skeleton.core.data

import com.santimattius.kmp.skeleton.MovieEntity
import com.santimattius.kmp.skeleton.core.data.datasources.MovieLocalDataSource
import com.santimattius.kmp.skeleton.core.data.datasources.MoviesRemoteDataSource
import com.santimattius.kmp.skeleton.core.data.models.asMovies
import com.santimattius.kmp.skeleton.core.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.santimattius.kmp.skeleton.core.data.models.Movie as MovieDto

class MovieRepository(
    private val remote: MoviesRemoteDataSource,
    private val local: MovieLocalDataSource
) {

    val all: Flow<List<Movie>>
        get() = local.all.map { entities ->
            entities.map { movie ->
                Movie(
                    id = movie.id,
                    title = movie.title,
                    image = movie.image,
                    overview = movie.overview
                )
            }
        }

    suspend fun getMovies(): List<Movie> {
        // TODO: add error handling
        val result: List<MovieDto> = remote.getMovies().getOrThrow()
        return result.asMovies()
    }

    suspend fun refresh(): Result<Unit> {
        return remote.getMovies().fold(
            onSuccess = { remoteMovies ->
                val movies = remoteMovies.map {
                    MovieEntity(it.id, it.title, it.poster, it.overview, false)
                }
                local.insertOrUpdate(movies)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}
