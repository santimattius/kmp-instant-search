package com.santimattius.kmp.skeleton.core.data.datasources.ktor

import com.santimattius.kmp.skeleton.core.data.datasources.MoviesRemoteDataSource
import com.santimattius.kmp.skeleton.core.data.models.MovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.santimattius.kmp.skeleton.core.data.models.Movie as MovieDto

class KtorMoviesRemoteDataSource(
    private val client: HttpClient,
) : MoviesRemoteDataSource {

    override suspend fun getMovies(): Result<List<MovieDto>> = runCatching {
        val response = client.get("movie/popular")
        val result = response.body<MovieResponse>()
        result.results
    }

}