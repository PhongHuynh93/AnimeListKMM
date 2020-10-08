package com.wind.animelist.shared.util

/**
 * Created by Phong Huynh on 10/8/2020
 */
interface StringRetriever {
    fun string(resourceId: Int): Lazy<String>
}