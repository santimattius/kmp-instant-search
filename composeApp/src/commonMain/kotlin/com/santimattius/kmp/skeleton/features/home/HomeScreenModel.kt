package com.santimattius.kmp.skeleton.features.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.santimattius.kmp.skeleton.core.data.MovieRepository
import com.santimattius.kmp.skeleton.core.domain.Movie
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState = _searchQuery
        .debounce(600)
        .map { it.trim().lowercase() }
        .distinctUntilChanged()
        .flatMapLatest { searchQuery ->
            repository.search(searchQuery)
        }
        .map { HomeUiState(isLoading = false, data = it) }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )

    init {
        refresh()
    }

    private fun refresh() {
        screenModelScope.launch(exceptionHandler) {
            repository.refresh()
        }
    }

    fun onSearch(searchQuery: String) {
        _searchQuery.update { searchQuery }
    }
}