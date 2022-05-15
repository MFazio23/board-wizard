package dev.mfazio.boardwizard.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.mfazio.boardwizard.R

sealed class BoardWizardNavScreen(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
) {
    object GameList : BoardWizardNavScreen(
        "GameList",
        R.string.game_list,
        R.drawable.ic_baseline_view_list_24
    )

    object Randomizer : BoardWizardNavScreen(
        "Randomizer",
        R.string.randomizer,
        R.drawable.ic_baseline_casino_24
    )

    object GamePlays : BoardWizardNavScreen(
        "GamePlays",
        R.string.game_plays,
        R.drawable.ic_baseline_playlist_play_24
    )
}
