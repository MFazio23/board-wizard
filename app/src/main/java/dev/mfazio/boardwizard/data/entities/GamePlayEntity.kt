package dev.mfazio.boardwizard.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mfazio.bgg.api.model.remote.plays.BGGPlayRemote

@Entity(tableName = "game_plays")
data class GamePlayEntity(
    @PrimaryKey(autoGenerate = true) val playId: Long = 0,
    val playDate: String,
    val quantity: Int,
    val length: Int,
    val isIncomplete: Boolean,
    val isNowInStats: Boolean,
    val location: String,
    val comments: String? = null,
    val winnerName: String? = null,
    val gameName: String,
    val bggGameId: Long?,
) {
    companion object {
        fun fromAPIModel(apiModel: BGGPlayRemote) = GamePlayEntity(
            playDate = apiModel.playDate,
            quantity = apiModel.quantity,
            length = apiModel.length,
            isIncomplete = apiModel.incomplete,
            isNowInStats = apiModel.nowInStats,
            location = apiModel.location,
            comments = apiModel.comments,
            winnerName = apiModel.players?.firstOrNull { it.didWin }?.name,
            gameName = apiModel.item.itemName,
            bggGameId = apiModel.item.objectId.toLongOrNull()
        )
    }
}
