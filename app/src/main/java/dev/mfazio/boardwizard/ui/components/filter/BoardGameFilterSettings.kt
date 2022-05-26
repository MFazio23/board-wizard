package dev.mfazio.boardwizard.ui.components.filter

import dev.mfazio.boardwizard.models.BoardGameFilter

data class BoardGameFilterSettings(
    val players: Int,
    val isPlayersEnabled: Boolean,
    val youngestPlayer: Int,
    val isYoungestPlayerEnabled: Boolean,
    val weights: List<BoardGameFilter.Weights> = emptyList(),
    val playingTimes: List<BoardGameFilter.PlayTimes> = emptyList(),
) {
    companion object {
        val default = BoardGameFilterSettings(
            players = 3,
            isPlayersEnabled = false,
            youngestPlayer = 8,
            isYoungestPlayerEnabled = false,
        )
    }
}
