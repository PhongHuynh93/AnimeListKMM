package com.wind.animelist.shared.domain.mapper.impl

import com.wind.animelist.shared.data.model.NetworkMoreInfo
import com.wind.animelist.shared.domain.mapper.Mapper
import com.wind.animelist.shared.domain.model.MoreInfo

/**
 * Created by Phong Huynh on 10/10/2020
 */
class MoreInfoDataMapper : Mapper<NetworkMoreInfo, MoreInfo> {
    override fun map(input: NetworkMoreInfo): MoreInfo {
        return MoreInfo(
            text = input.moreinfo.orEmpty(),
        )
    }
}