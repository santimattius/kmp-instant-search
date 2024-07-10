package com.santimattius.kmp.skeleton.core.data.datasources

import com.santimattius.kmp.skeleton.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    val all: Flow<List<MovieEntity>>

    suspend fun insertOrUpdate(movies: List<MovieEntity>): Result<Unit>

    fun search(query: String): Flow<List<MovieEntity>>
}