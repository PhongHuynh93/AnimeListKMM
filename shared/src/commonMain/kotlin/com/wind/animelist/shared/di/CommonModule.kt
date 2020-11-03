package com.wind.animelist.shared.di

import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.data.RepositoryImpl
import com.wind.animelist.shared.domain.usecase.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Created by Phong Huynh on 10/12/2020
 */

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule)
}

// called by iOS etc
fun initKoin() = initKoin{}

val commonModule = module {
    single<Repository> {
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
        RepositoryImpl(httpClient)}
    factory { GetTopMangaUseCase(ioDispatcher, get()) }
    factory { GetTopAnimeUseCase(ioDispatcher, get()) }
    factory { GetCharacterInMangaUseCase(ioDispatcher, get()) }
    factory { GetMoreInfoUseCase(ioDispatcher, get()) }
    factory { GetMangaHomeUseCase(ioDispatcher, get()) }
}