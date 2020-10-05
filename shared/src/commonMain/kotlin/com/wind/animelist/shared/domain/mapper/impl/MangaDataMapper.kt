package com.wind.animelist.shared.domain.mapper.impl

import com.wind.animelist.shared.data.model.NetworkManga
import com.wind.animelist.shared.domain.mapper.Mapper
import com.wind.animelist.shared.domain.model.Manga

/**
 * Created by Phong Huynh on 10/3/2020
 */
class MangaDataMapper: Mapper<NetworkManga, Manga> {
    override fun map(input: NetworkManga): Manga {
        return Manga(
            id = input.id ?: 0,
            title = input.title.orEmpty(),
            imageUrl = input.imageUrl.orEmpty().let { originalUrl ->
                if (originalUrl.isEmpty()) {
                    originalUrl
                } else {
                    val pos = originalUrl.indexOf('?')
                    if (pos > 0) {
                        originalUrl.substring(0, pos).let {
                            val posDot = it.lastIndexOf('.')
                            if (posDot > 0) {
                                it.substring(0, posDot) + 'l' + it.substring(posDot, it.length)
                            } else {
                                originalUrl
                            }
                        }
                    } else {
                        originalUrl
                    }
                }
            }
        )
    }
}