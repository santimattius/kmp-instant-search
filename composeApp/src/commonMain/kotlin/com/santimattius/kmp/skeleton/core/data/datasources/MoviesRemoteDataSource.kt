package com.santimattius.kmp.skeleton.core.data.datasources

import com.santimattius.kmp.skeleton.core.data.models.Movie

interface MoviesRemoteDataSource {
    suspend fun getMovies(): Result<List<Movie>>
}