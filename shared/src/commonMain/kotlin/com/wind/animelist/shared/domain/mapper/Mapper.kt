package com.wind.animelist.shared.domain.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}