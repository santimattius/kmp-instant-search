package com.santimattius.kmp.skeleton.di.core.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.santimattius.kmp.skeleton.MoviesDatabase

internal fun createDriver(): SqlDriver {
    return NativeSqliteDriver(MoviesDatabase.Schema, "app_database.db")
}