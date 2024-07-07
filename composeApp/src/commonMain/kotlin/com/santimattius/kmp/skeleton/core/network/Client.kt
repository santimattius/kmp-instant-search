package com.santimattius.kmp.skeleton.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger as KermitLogger

internal fun ktorHttpClient(
    domain: String,
    apiKey: String,
    version: String = "3"
) = HttpClient {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                KermitLogger.d("Ktor") { message }
            }

        }
        level = LogLevel.ALL
    }

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = domain
            path("$version/")
            parameters.append("api_key", apiKey)
        }
        contentType(ContentType.Application.Json)
    }
}