package dev.mfazio.boardwizard.ui.screens.randomizer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mfazio.boardwizard.data.BoardWizardRepository
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.ui.components.filter.BoardGameFilterSettings
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RandomizerViewModel @Inject constructor(
    private val boardWizardRepository: BoardWizardRepository,
) : ViewModel() {
    var currentGame: BoardGame? by mutableStateOf(null)
        private set

    var boardGameFilterSettings by mutableStateOf(BoardGameFilterSettings.default)

    fun getRandomGame() {
        viewModelScope.launch {
            val filteredGames = boardWizardRepository.getFilteredBoardGames(
                listOfNotNull(
                    if (boardGameFilterSettings.isPlayersEnabled) {
                        BoardGameFilter.PlayerCount(boardGameFilterSettings.players)
                    } else null,
                    if (boardGameFilterSettings.isYoungestPlayerEnabled) {
                        BoardGameFilter.YoungestPlayer(boardGameFilterSettings.youngestPlayer)
                    } else null,
                    BoardGameFilter.FilteredWeights(*boardGameFilterSettings.weights.toTypedArray()),
                    BoardGameFilter.FilteredPlayTimes(*boardGameFilterSettings.playingTimes.toTypedArray()),
                )
            )

            currentGame = filteredGames.shuffled().firstOrNull()
        }
    }

    fun clearCurrentGame() {
        currentGame = null
    }
}