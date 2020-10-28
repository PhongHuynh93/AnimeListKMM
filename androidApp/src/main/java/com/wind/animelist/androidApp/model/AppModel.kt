package com.wind.animelist.androidApp.model

import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.DetailManga
import com.wind.animelist.shared.viewmodel.model.Home

/**
 * Created by Phong Huynh on 10/18/2020
 */
data class Title(val text: String): Home, DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_TITLE
    }
}

object Divider: Home {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_DIVIDER
    }
}