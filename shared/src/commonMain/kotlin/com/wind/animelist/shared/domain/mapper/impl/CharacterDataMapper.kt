package com.wind.animelist.shared.domain.mapper.impl

import com.wind.animelist.shared.data.model.NetworkCharacter
import com.wind.animelist.shared.domain.mapper.Mapper
import com.wind.animelist.shared.domain.model.Character

/**
 * Created by Phong Huynh on 10/10/2020
 */
class CharacterDataMapper : Mapper<NetworkCharacter, Character> {
    override fun map(input: NetworkCharacter): Character {
        return Character(
            id = input.id ?: 0,
            imageUrl = input.imageUrl.orEmpty(),
            title = input.name.orEmpty(),
            role = input.role.orEmpty()
        )
    }
}