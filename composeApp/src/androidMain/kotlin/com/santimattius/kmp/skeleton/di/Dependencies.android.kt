package com.santimattius.kmp.skeleton.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.santimattius.kmp.skeleton.core.db.createDriver
import com.santimattius.kmp.skeleton.startup.applicationContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform: Module
    get() = module {
        single<Context> {
            val context = applicationContext ?: run {
                throw IllegalArgumentException("Application context is null")
            }
            context
        }

        single<SqlDriver> { createDriver(appContext = get()) }
    }