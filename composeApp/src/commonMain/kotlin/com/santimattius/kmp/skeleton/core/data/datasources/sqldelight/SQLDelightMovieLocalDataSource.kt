package com.santimattius.kmp.skeleton.core.data.datasources.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.santimattius.kmp.skeleton.MovieEntity
import com.santimattius.kmp.skeleton.MoviesDatabase
import com.santimattius.kmp.skeleton.core.data.datasources.MovieLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SQLDelightMovieLocalDataSource(
    db: MoviesDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieLocalDataSource {

    private val queries = db.moviesDatabaseQueries

    override val all: Flow<List<MovieEntity>>
        get() = queries.selectAll().asFlow().mapToList(dispatcher)

    override suspend fun insertOrUpdate(movies: List<MovieEntity>) = runCatching {
        queries.transaction {
            movies.forEach { movie ->
                val (id, title, image, overview) = movie
                queries.insert(id, title, image, overview)
            }
        }
    }

    override fun search(query: String): Flow<List<MovieEntity>> {
        return if (query.isEmpty()) {
            all
        } else {
            queries.search(queryString = query)
                .asFlow()
                .mapToList(dispatcher).map {
                    it.map { movie ->
                        MovieEntity(
                            id = movie.id,
                            title = movie.title,
                            image = movie.image,
                            overview = movie.overview,
                            favorite = movie.favorite
                        )
                    }
                }
        }
    }
}