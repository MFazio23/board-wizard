package dev.mfazio.boardwizard.data

import androidx.lifecycle.LiveData
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.models.GamePlay
import kotlinx.coroutines.flow.Flow

interface BoardWizardRepository {
    suspend fun getBGGUserName(): String?
    suspend fun getBGGUserNameFlow(): Flow<String?>
    fun getBGGUserNameLiveData(): LiveData<String?>
    suspend fun getAllBoardGames(): List<BoardGame>
    fun getAllBoardGamesLiveData(): LiveData<List<BoardGame>>
    fun getBoardGameById(id: Int): LiveData<BoardGame>
    suspend fun getFilteredBoardGames(filters: List<BoardGameFilter>): List<BoardGame>
    suspend fun getFilteredBoardGames(
        games: List<BoardGame>,
        filters: List<BoardGameFilter>
    ): List<BoardGame>

    suspend fun getBoardGameCount(): Int
    suspend fun hasSavedBoardGames(): Boolean
    suspend fun getBoardGameByIdFromAPI(id: Int): LiveData<BoardGame>
    suspend fun loadGamesFromBGG(): Int
    suspend fun isUserNameNeeded(): Boolean
    suspend fun updateUserName(userName: String): String?

    fun getAllGamePlays(): LiveData<List<GamePlay>>

    suspend fun loadGamePlaysFromBGG(): Int
    suspend fun getGamePlayCount(): Int

    val repoTestValue: String
}