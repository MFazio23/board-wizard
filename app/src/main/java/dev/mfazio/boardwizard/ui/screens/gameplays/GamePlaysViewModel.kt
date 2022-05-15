package dev.mfazio.boardwizard.ui.screens.gameplays

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
class GamePlaysViewModel @Inject constructor(
    private val boardWizardRepository: BoardWizardRepository,
) : ViewModel() {
    val gamePlays = boardWizardRepository.getAllGamePlays()

    var isRefreshing by mutableStateOf(false)
        private set

    fun refreshGamePlayList() {
        viewModelScope.launch {
            isRefreshing = true
            boardWizardRepository.loadGamePlaysFromBGG()
            isRefreshing = false
        }
    }
}