package com.santimattius.kmp.skeleton.di

import com.santimattius.kmp.skeleton.BuildConfig
import com.santimattius.kmp.skeleton.MoviesDatabase
import com.santimattius.kmp.skeleton.core.data.MovieRepository
import com.santimattius.kmp.skeleton.core.data.datasources.MovieLocalDataSource
import com.santimattius.kmp.skeleton.core.data.datasources.MoviesRemoteDataSource
import com.santimattius.kmp.skeleton.core.data.datasources.ktor.KtorMoviesRemoteDataSource
import com.santimattius.kmp.skeleton.core.data.datasources.sqldelight.SQLDelightMovieLocalDataSource
import com.santimattius.kmp.skeleton.core.db.createDatabase
import com.santimattius.kmp.skeleton.core.network.ktorHttpClient
import com.santimattius.kmp.skeleton.features.home.HomeScreenModel
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val sharedModules = module {
    //Infrastructure
    single(qualifier(AppQualifiers.Domain)) { "api.themoviedb.org" }
    single(qualifier(AppQualifiers.Client)) {
        ktorHttpClient(
            domain = get(qualifier(AppQualifiers.Domain)),
            apiKey = BuildConfig.apiKey
        )
    }
    single<MoviesDatabase> { createDatabase(driver = get()) }
    //DataSources
    single<MoviesRemoteDataSource> { KtorMoviesRemoteDataSource(client = get(qualifier(AppQualifiers.Client))) }
    single<MovieLocalDataSource> { SQLDelightMovieLocalDataSource(db = get()) }

    //Repositories
    single { MovieRepository(remote = get(), local=get()) }
}

val homeModule = module {
    factory { HomeScreenModel(repository = get()) }
}

expect val platform: Module

fun applicationModules() = listOf(platform, sharedModules, homeModule)