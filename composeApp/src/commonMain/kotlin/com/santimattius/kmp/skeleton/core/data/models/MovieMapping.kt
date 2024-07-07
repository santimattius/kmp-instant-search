package com.santimattius.kmp.skeleton.core.data.models

import com.santimattius.kmp.skeleton.core.domain.Movie
import com.santimattius.kmp.skeleton.core.data.models.Movie as MovieDto

internal fun MovieDto.asMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        image = this.poster,
        overview = this.overview
    )
}


internal fun List<MovieDto>.asMovies(): List<Movie> {
    return this.map { it.asMovie() }
}
