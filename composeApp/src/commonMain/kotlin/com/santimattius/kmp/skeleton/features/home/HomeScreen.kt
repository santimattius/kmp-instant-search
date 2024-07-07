package com.santimattius.kmp.skeleton.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.santimattius.kmp.skeleton.core.domain.Movie
import com.santimattius.kmp.skeleton.core.ui.components.AppBar
import com.santimattius.kmp.skeleton.core.ui.components.LoadingIndicator
import com.santimattius.kmp.skeleton.core.ui.components.Message
import com.santimattius.kmp.skeleton.core.ui.components.NetworkImage

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        HomeScreenContent(screenModel)
    }
}

@Composable
fun HomeScreenContent(
    screenModel: HomeScreenModel,
) {
    Scaffold(
        topBar = {
            AppBar(title = "Compose Skeleton")
//            SearchableTopAppBar(
//                text = searchQuery,
//                onTextChange = { text ->
//                    searchQuery = text
//                },
//                placeholder = "type your search here",
//                title = "Compose Skeleton"
//            )
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {

            val state by screenModel.state.collectAsState()
            when {
                state.isLoading -> LoadingIndicator()

                state.hasError -> {
                    Message(message = "An error occurred while updating the movies")
                }

                state.data.isEmpty() -> {
                    Message(message = "Empty")
                }

                else -> {
                    MovieListView(state.data)
                }
            }
        }
    }
}

@Composable
fun MovieListView(movies: List<Movie>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieItemView(movie = movie)
        }
    }
}

private const val IMAGE_ASPECT_RATIO = 0.67f

@Composable
fun MovieItemView(movie: Movie, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier.wrapContentHeight(Alignment.Top),
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        leadingContent = {
            NetworkImage(
                imageUrl = movie.image,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .background(Color.LightGray)
                    .aspectRatio(ratio = IMAGE_ASPECT_RATIO),
            )
        },
        headlineContent = {
            Text(text = movie.title)
        },
        supportingContent = {
            Text(
                text = movie.overview,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
            )
        }
    )
}
