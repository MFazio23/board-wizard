package dev.mfazio.boardwizard.ui.animation

import androidx.compose.ui.graphics.Color

data class LottiePropertySpec(
    val colors: List<Color>,
    val layerName: String,
    val groupCount: Int,
    val fillCount: Int,
)
