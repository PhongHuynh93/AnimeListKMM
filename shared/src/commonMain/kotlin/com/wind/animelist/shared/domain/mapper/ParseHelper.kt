package com.wind.animelist.shared.domain.mapper

// TODO: 10/29/2020 check this again
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