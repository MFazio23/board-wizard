package dev.mfazio.boardwizard.ui.screens.randomizer

import dev.mfazio.boardwizard.models.BoardGameFilter

data class BoardGameFilterSettings(
    val players: Int,
    val minimumAge: Int,
    val weights: List<BoardGameFilter.Weights> = emptyList(),
    val playingTimes: List<BoardGameFilter.PlayTimes> = emptyList(),
) {
    companion object {
        val default = BoardGameFilterSettings(
            players = 3,
            minimumAge = 8,
        )
    }
}
