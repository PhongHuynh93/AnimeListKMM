package com.wind.animelist.shared.viewmodel.di

import com.wind.animelist.shared.domain.di.ioDispatcherTag
import com.wind.animelist.shared.domain.usecase.GetCharacterInMangaUseCase
import com.wind.animelist.shared.domain.usecase.GetMoreInfoUseCase
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

/**
 * Created by Phong Huynh on 10/4/2020
 */
val homeVModule = DI.Module("homeVM") {
    bind<GetTopMangaUseCase>() with provider {
        GetTopMangaUseCase(instance(ioDispatcherTag), instance())
    }
    bind<GetTopAnimeUseCase>() with provider {
        GetTopAnimeUseCase(instance(ioDispatcherTag), instance())
    }
}

val moreVModule = DI.Module("moreVM") {
    bind<GetTopMangaUseCase>() with provider {
        GetTopMangaUseCase(instance(ioDispatcherTag), instance())
    }
    bind<GetTopAnimeUseCase>() with provider {
        GetTopAnimeUseCase(instance(ioDispatcherTag), instance())
    }
}

val detailMangaVModule = DI.Module("detailMangaVM") {
    bind<GetCharacterInMangaUseCase>() with provider {
        GetCharacterInMangaUseCase(instance(ioDispatcherTag), instance())
    }
    bind<GetMoreInfoUseCase>() with provider {
        GetMoreInfoUseCase(instance(ioDispatcherTag), instance())
    }
}