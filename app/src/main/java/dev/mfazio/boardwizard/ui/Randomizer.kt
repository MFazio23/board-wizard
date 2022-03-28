package dev.mfazio.boardwizard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Randomizer() {
    Column {
        Text(text = "Game title")
        Text(text = "Info area")
        Text(text = "Randomizer")
        Text(text = "Filter")
    }
}