package dev.mfazio.boardwizard.ui.screens.gamelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mfazio.boardwizard.data.BoardWizardRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val boardWizardRepository: BoardWizardRepository,
) : ViewModel() {
    val games = boardWizardRepository.getAllBoardGamesLiveData()

    val userName = boardWizardRepository.getBGGUserNameLiveData()

    var isRefreshing by mutableStateOf(false)
        private set

    fun refreshGameList() {
        viewModelScope.launch {
            isRefreshing = true
            boardWizardRepository.loadGamesFromBGG()
            isRefreshing = false
        }
    }
}