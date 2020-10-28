package com.wind.animelist.shared.domain.mapper

fun parseSubType(title : String) : Any {
    val type = title.toLowerCase().trim()
    return if(type == "top manga") {
        "manga"
    } else if (type == "top novel") {
        "novels"
    } else if (type == "top doujin") {
        "doujin"
    } else if (type == "top manhwa") {
        "manhwa"
    } else if (type == "top manhua") {
        "manhua"
    } else {
        "upcoming"
    }
}