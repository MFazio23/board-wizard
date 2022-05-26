package dev.mfazio.boardwizard.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mfazio.boardwizard.data.BoardWizardRepository
import kotlinx.coroutines.delay
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

    var snackBarMessage by mutableStateOf<String?>(null)
        private set

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
            snackBarMessage = "Loading games from BGG..."
            Timber.tag("BGG").i("Loading games from BGG")
            val loadedGameCount = boardWizardRepository.loadGamesFromBGG()

            if (loadedGameCount == 0) {
                snackBarMessage = "Error loading games from BGG. Please restart the app."
                delay(2000)
                snackBarMessage = null
            } else {
                snackBarMessage = "Games loaded successfully!"
                delay(2000)
                snackBarMessage = null
            }
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