package dev.mfazio.boardwizard.models

import dev.mfazio.boardwizard.data.entities.BoardGameEntity

data class BoardGame(
    val bggId: Long,
    val title: String?,
    val minPlayers: Int?,
    val maxPlayers: Int?,
    val minTime: Int?,
    val maxTime: Int?,
    val officialMinAge: Int?,
    val communityMinAge: Int?,
    val weight: Double?,
    val imageUrl: String?,
    val thumbnailUrl: String?,
    val playCount: Int? = null,
) {
    companion object {
        fun fromEntity(entity: BoardGameEntity) = BoardGame(
            bggId = entity.bggId,
            title = entity.title,
            minPlayers = entity.minPlayers,
            maxPlayers = entity.maxPlayers,
            minTime = entity.minTime,
            maxTime = entity.maxTime,
            officialMinAge = entity.officialMinAge,
            communityMinAge = entity.communityMinAge,
            weight = entity.weight,
            imageUrl = entity.imageUrl,
            thumbnailUrl = entity.thumbnailUrl,
        )
    }
}