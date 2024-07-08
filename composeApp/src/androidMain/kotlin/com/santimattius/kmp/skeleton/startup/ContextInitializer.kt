package com.santimattius.kmp.skeleton.startup

import android.content.Context
import androidx.startup.Initializer

var applicationContext: Context? = null
    private set

class ContextInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}