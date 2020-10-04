package com.wind.data.di

import com.wind.data.Repository
import com.wind.data.RepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

/**
 * Created by Phong Huynh on 9/24/2020
 */
val dataModule = DI.Module("data") {
    bind<Repository>() with singleton {
        val httpClient = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
        }
        RepositoryImpl(httpClient)
    }
}