package com.wind.animelist.shared.domain.model

/**
 * Created by Phong Huynh on 11/1/2020
 */
class Response<R>(
    var data: R,
    var loadMoreInfo: LoadMoreInfo,
    var page: Int,
    var isMore: Boolean = false,
)
