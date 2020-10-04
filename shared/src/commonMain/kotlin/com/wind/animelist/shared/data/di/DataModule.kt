package com.wind.animelist.shared.data.di

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.data.RepositoryImpl
import io.ktor.client.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

/**
 * Created by Phong Huynh on 9/24/2020
 */
val dataModule = DI.Module("data") {
    bind<Repository>() with singleton {
        val httpClient = HttpClient {
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