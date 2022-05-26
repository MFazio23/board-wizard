package dev.mfazio.boardwizard.ui.screens.gamelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mfazio.boardwizard.data.BoardWizardRepository
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.ui.components.filter.BoardGameFilterSettings
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val boardWizardRepository: BoardWizardRepository,
) : ViewModel() {
    private val games = boardWizardRepository.getAllBoardGamesLiveData()

    val filteredGames = MediatorLiveData<List<BoardGame>>()

    val boardGameFilterSettings = MutableLiveData(BoardGameFilterSettings.default)

    init {
        filteredGames.addSource(games) { newGames ->
            updateFilteredGames(
                games = newGames,
                filterSettings = boardGameFilterSettings.value
            )
        }
        filteredGames.addSource(boardGameFilterSettings) { newFilters ->
            updateFilteredGames(
                games = games.value ?: emptyList(),
                filterSettings = newFilters
            )
        }
    }

    var isRefreshing by mutableStateOf(false)
        private set

    fun refreshGameList() {
        viewModelScope.launch {
            isRefreshing = true
            boardWizardRepository.loadGamesFromBGG()
            isRefreshing = false
        }
    }

    private fun updateFilteredGames(
        games: List<BoardGame>,
        filterSettings: BoardGameFilterSettings?
    ) = viewModelScope.launch {
        filteredGames.value = if (filterSettings != null) {
            val filters = listOfNotNull(
                if (filterSettings.isPlayersEnabled) {
                    BoardGameFilter.PlayerCount(filterSettings.players)
                } else null,
                if (filterSettings.isYoungestPlayerEnabled) {
                    BoardGameFilter.YoungestPlayer(filterSettings.youngestPlayer)
                } else null,
                BoardGameFilter.FilteredWeights(*filterSettings.weights.toTypedArray()),
                BoardGameFilter.FilteredPlayTimes(*filterSettings.playingTimes.toTypedArray()),
            )
            boardWizardRepository.getFilteredBoardGames(games, filters)
        } else games
    }

    fun updateFilterSettings(filterSettings: BoardGameFilterSettings) {
        boardGameFilterSettings.value = filterSettings
    }
}