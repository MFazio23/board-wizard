package dev.mfazio.boardwizard.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mfazio.bgg.api.model.remote.thing.BGGThingRemote
import dev.mfazio.bgg.api.model.remote.thing.poll.BGGThingPollRemote

@Entity(tableName = "board_games")
data class BoardGameEntity(
    @PrimaryKey val bggId: Long,
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
) {
    companion object {
        fun fromAPIModel(apiModel: BGGThingRemote): BoardGameEntity {
            apiModel.thingId.let { bggId ->
                val communityMinAge = getCommunityMinAge(
                    apiModel.polls?.firstOrNull { it.name == "suggested_playerage" }
                )
                return BoardGameEntity(
                    bggId = bggId,
                    title = apiModel.names?.firstOrNull { it.nameType == "primary" }?.name,
                    minPlayers = apiModel.minPlayers.value,
                    maxPlayers = apiModel.maxPlayers.value,
                    minTime = apiModel.minPlayTime.value,
                    maxTime = apiModel.maxPlayTime.value,
                    officialMinAge = apiModel.minimumAge.value,
                    communityMinAge = communityMinAge,
                    weight = apiModel.statistics?.ratings?.averageWeight?.value,
                    imageUrl = apiModel.imageUrl,
                    thumbnailUrl = apiModel.thumbnailUrl,
                )
            }
        }

        private fun getCommunityMinAge(communityAgePoll: BGGThingPollRemote?): Int? =
            communityAgePoll
                ?.pollResults
                ?.firstOrNull()
                ?.pollResults
                ?.maxByOrNull { it.numberOfVotes ?: 0 }
                ?.value
                ?.toIntOrNull()
    }
}
