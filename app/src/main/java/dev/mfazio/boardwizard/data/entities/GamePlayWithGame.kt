package dev.mfazio.boardwizard.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class GamePlayWithGame(
    @Embedded val gamePlay: GamePlayEntity,
    @Relation(
        parentColumn = "bggGameId",
        entityColumn = "bggId"
    )
    val game: BoardGameEntity?,
)
