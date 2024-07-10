package com.santimattius.kmp.skeleton.core.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

import com.santimattius.kmp.skeleton.MoviesDatabase
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory

internal fun createDriver(appContext: Context): SqlDriver {
    val factory = RequerySQLiteOpenHelperFactory(emptyList())
    return AndroidSqliteDriver(
        schema = MoviesDatabase.Schema,
        context = appContext,
        name = "app_database.db",
        factory = factory
    )
}