package com.santimattius.kmp.skeleton.di

import app.cash.sqldelight.db.SqlDriver
import com.santimattius.kmp.skeleton.di.core.db.createDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform: Module
    get() = module {
        single<SqlDriver> { createDriver() }
    }