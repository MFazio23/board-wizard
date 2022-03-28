package dev.mfazio.boardwizard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

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
        R.drawable.ic_baseline_view_list_24
    )

    object PlayerList : BoardWizardNavScreen(
        "PlayerList",
        R.string.player_list,
        R.drawable.ic_baseline_view_list_24
    )
}
