package com.santimattius.kmp.skeleton.features.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.santimattius.kmp.skeleton.core.data.MovieRepository
import com.santimattius.kmp.skeleton.core.domain.Movie
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val data: List<Movie> = emptyList(),
)

class HomeScreenModel(
    private val repository: MovieRepository,
) : StateScreenModel<HomeUiState>(HomeUiState()) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        mutableState.update { it.copy(isLoading = false, hasError = true) }
    }

    init {
        refresh()
    }

    private fun refresh() {
        mutableState.update { it.copy(isLoading = true, hasError = false) }
        screenModelScope.launch(exceptionHandler) {
            val movies = repository.getMovies()
            mutableState.update { it.copy(isLoading = false, data = movies) }
        }
    }
}