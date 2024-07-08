package com.santimattius.kmp.skeleton.core.db

import app.cash.sqldelight.db.SqlDriver
import com.santimattius.kmp.skeleton.MoviesDatabase

internal fun createDatabase(driver: SqlDriver): MoviesDatabase {
    return MoviesDatabase(driver)
}