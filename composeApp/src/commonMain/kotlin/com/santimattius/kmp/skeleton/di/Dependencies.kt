package com.santimattius.kmp.skeleton.di

import com.santimattius.kmp.skeleton.BuildConfig
import com.santimattius.kmp.skeleton.core.data.MovieRepository
import com.santimattius.kmp.skeleton.core.data.datasources.RemoteMoviesDataSource
import com.santimattius.kmp.skeleton.core.data.datasources.ktor.KtorRemoteMoviesDataSource
import com.santimattius.kmp.skeleton.core.network.ktorHttpClient
import com.santimattius.kmp.skeleton.features.home.HomeScreenModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val sharedModules = module {
    single(qualifier(AppQualifiers.Domain)) { "api.themoviedb.org" }
    single(qualifier(AppQualifiers.Client)) {
        ktorHttpClient(
            domain = get(qualifier(AppQualifiers.Domain)),
            apiKey = BuildConfig.apiKey
        )
    }
    single<RemoteMoviesDataSource> { KtorRemoteMoviesDataSource(client = get(qualifier(AppQualifiers.Client))) }

    single { MovieRepository(remoteMoviesDataSource = get()) }
}

val homeModule = module {
    factory { HomeScreenModel(repository = get()) }
}


fun applicationModules() = listOf(sharedModules, homeModule)