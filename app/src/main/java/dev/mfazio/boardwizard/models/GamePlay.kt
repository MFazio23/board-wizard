package dev.mfazio.boardwizard.models

import java.time.LocalDate

data class GamePlay(
    val gamePlayId: Long,
    val gameName: String,
    val playDate: LocalDate,
    val location: String,
    val thumbnailUrl: String? = null,
    val winnerName: String? = null,
    val comments: String? = null,
)
