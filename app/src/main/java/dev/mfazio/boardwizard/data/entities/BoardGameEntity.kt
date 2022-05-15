package dev.mfazio.boardwizard.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mfazio.bgg.api.model.remote.collection.BGGItemRemote
import dev.mfazio.bgg.api.model.remote.thing.BGGThingRemote

@Entity(tableName = "board_games")
data class BoardGameEntity(
    @PrimaryKey val bggId: Long,
    val title: String?,
    val minPlayers: Int?,
    val maxPlayers: Int?,
    val minTime: Int?,
    val maxTime: Int?,
    val minAge: Int?,
    val weight: Double?,
    val imageUrl: String?,
    val thumbnailUrl: String?,
) {
    companion object {
        fun fromAPIModel(apiModel: BGGThingRemote): BoardGameEntity {
            apiModel.thingId.let { bggId ->
                return BoardGameEntity(
                    bggId = bggId,
                    title = apiModel.names?.firstOrNull { it.nameType == "primary" }?.name,
                    minPlayers = apiModel.minPlayers.value,
                    maxPlayers = apiModel.maxPlayers.value,
                    minTime = apiModel.minPlayTime.value,
                    maxTime = apiModel.maxPlayTime.value,
                    minAge = apiModel.minimumAge.value,
                    weight = apiModel.statistics?.ratings?.averageWeight?.value,
                    imageUrl = apiModel.imageUrl,
                    thumbnailUrl = apiModel.thumbnailUrl,
                )
            }
        }
    }
}
