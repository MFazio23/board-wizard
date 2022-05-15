package dev.mfazio.boardwizard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mfazio.boardwizard.data.BoardWizardRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val boardWizardRepository: BoardWizardRepository,
) : ViewModel() {

    val userName: LiveData<String?>
        get() = _userName
    private val _userName = boardWizardRepository.getBGGUserNameLiveData()

    fun updateUserName(userName: String) = viewModelScope.launch {
        boardWizardRepository.updateUserName(userName)

        updateGamesIfNecessary()
        updateGamePlaysIfNecessary()
    }

    fun updateGamesIfNecessary() = viewModelScope.launch {
        val gameCount = boardWizardRepository.getBoardGameCount()
        val hasSavedBoardGames = boardWizardRepository.hasSavedBoardGames()
        val isUserNameNeeded = boardWizardRepository.isUserNameNeeded()

        Timber.tag("BGG").i("updateGamesIfNecessary: BG Count=$gameCount")
        if (!isUserNameNeeded && !hasSavedBoardGames) {
            Timber.tag("BGG").i("Loading games from BGG")
            boardWizardRepository.loadGamesFromBGG()
        } else {
            Timber
                .tag("BGG")
                .i("No load needed: UserName needed = $isUserNameNeeded, Saved Games = $hasSavedBoardGames")
        }
    }

    fun updateGamePlaysIfNecessary() = viewModelScope.launch {
        val gamePlayCount = boardWizardRepository.getGamePlayCount()
        val isUserNameNeeded = boardWizardRepository.isUserNameNeeded()

        Timber.tag("BGG").i("updateGamePlaysIfNecessary: Play Count=$gamePlayCount")
        if (!isUserNameNeeded && gamePlayCount == 0) {
            Timber.tag("BGG").i("Loading game plays from BGG")
            boardWizardRepository.loadGamePlaysFromBGG()
        } else {
            Timber
                .tag("BGG")
                .i("No load needed: UserName needed = $isUserNameNeeded, Game Play Count = $gamePlayCount")
        }

    }
}