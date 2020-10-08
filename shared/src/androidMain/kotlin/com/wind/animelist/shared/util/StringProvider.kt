package com.wind.animelist.shared.util

import android.content.Context

/**
 * Created by Phong Huynh on 10/8/2020
 */
class StringProvider (private val context: Context) : StringRetriever {
    override fun string(resourceId: Int) = lazy { context.getString(resourceId) }
}