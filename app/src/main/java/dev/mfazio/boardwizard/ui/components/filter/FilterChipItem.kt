package dev.mfazio.boardwizard.ui.components.filter

import androidx.compose.ui.graphics.Color

data class FilterChipItem<T>(
    val value: T,
    val text: String,
    val selectedBackgroundColor: Color,
    val selectedContentColor: Color,
    val isSelected: Boolean = false,
)
