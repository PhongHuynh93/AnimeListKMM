package com.wind.animelist.androidApp.util

import android.content.Context
import android.view.LayoutInflater
import com.wind.animelist.androidApp.R
import util.getDimen

/**
 * Created by Phong Huynh on 10/6/2020
 */
val Context.spacePrettySmall
    get() = getDimen(R.dimen.space_pretty_small).toInt()
val Context.spaceNormal
    get() = getDimen(R.dimen.space_normal).toInt()

