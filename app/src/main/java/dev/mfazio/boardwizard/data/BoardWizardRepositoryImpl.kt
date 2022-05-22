package dev.mfazio.boardwizard.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.mfazio.bgg.api.repositories.BGGXMLServiceRepository
import dev.mfazio.boardwizard.data.entities.BoardGameEntity
import dev.mfazio.boardwizard.data.entities.GamePlayEntity
import dev.mfazio.boardwizard.data.settings.BoardWizardSettings
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.models.GamePlay
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BoardWizardRepositoryImpl @Inject constructor(
    private val bggRepository: BGGXMLServiceRepository,
    private val dao: BoardWizardDAO,
    private val boardWizardSettings: BoardWizardSettings,
) : BoardWizardRepository {

    override val repoTestValue: String = "BoardWizard repository test value here."

    override suspend fun getBGGUserName(): String? = boardWizardSettings.userName()
    override suspend fun getBGGUserNameFlow(): Flow<String?> = boardWizardSettings.userNameFlow()
    override fun getBGGUserNameLiveData(): LiveData<String?> =
        boardWizardSettings.userNameLiveData()

    override suspend fun getAllBoardGames(): List<BoardGame> =
        dao.getAllBoardGames().sortedBy { it.title }.map { entity ->
            BoardGame.fromEntity(entity)
        }

    override fun getAllBoardGamesLiveData(): LiveData<List<BoardGame>> =
        Transformations.map(dao.getAllBoardGamesLiveData()) { entities ->
            entities.sortedBy { it.title }.map { BoardGame.fromEntity(it) }
        }

    override fun getBoardGameById(id: Int): LiveData<BoardGame> {
        return MutableLiveData()
    }

    override suspend fun getBoardGameByIdFromAPI(id: Int): LiveData<BoardGame> {
        getBGGUserName()?.let { userName ->
            val plays = bggRepository.getPlaysForUser(userName)

            Timber.tag("BGG").i("${plays.userName} ${plays.total}")
            println("${plays.userName} ${plays.total}")
        }
        // TODO: Actually do this.

        return MutableLiveData()
    }

    override suspend fun loadGamesFromBGG(): Int {
        val remoteCollection = boardWizardSettings.userName()?.let { userName ->
            Timber.tag("BGG").i("Loading games from BGG for $userName")
            bggRepository.getBoardGameCollectionWithDetails(userName)
        }

        val games = remoteCollection?.things?.map { thing ->
            BoardGameEntity.fromAPIModel(thing)
        } ?: emptyList()

        return dao.addOrUpdateGames(games).size
    }

    override suspend fun getFilteredBoardGames(filters: List<BoardGameFilter>): List<BoardGame> =
        getAllBoardGames().filter { game ->
            filters.all {
                it.filterFunction(game)
            }
        }

    override suspend fun isUserNameNeeded(): Boolean = boardWizardSettings.userName() == null

    override suspend fun getBoardGameCount(): Int = dao.getBoardGameCount()
    override suspend fun hasSavedBoardGames(): Boolean = dao.getBoardGameCount() > 0

    override suspend fun updateUserName(userName: String): String? =
        boardWizardSettings.userName(userName)

    override suspend fun loadGamePlaysFromBGG(): Int {
        val remoteGamePlays = boardWizardSettings.userName()?.let { userName ->
            Timber.tag("BGG").i("Loading game plays from BGG for $userName")
            bggRepository.getPlaysForUser(userName)
        }

        val gamePlays = remoteGamePlays?.plays?.map { play ->
            GamePlayEntity.fromAPIModel(play)
        } ?: emptyList()

        Timber.tag("BGG").i("Loaded ${gamePlays.size} game plays from BGG.")

        return dao.addOrUpdateGamePlays(gamePlays).size
    }

    override fun getAllGamePlays(): LiveData<List<GamePlay>> =
        Transformations.map(dao.getAllGamePlaysWithGames()) { gamePlaysWithGames ->
            gamePlaysWithGames.map { gamePlayWithGame ->
                val (gamePlay, game) = gamePlayWithGame

                GamePlay(
                    gamePlayId = gamePlay.playId,
                    gameName = gamePlay.gameName,
                    playDate = LocalDate.parse(gamePlay.playDate, gamePlayDateFormat),
                    location = gamePlay.location,
                    thumbnailUrl = game?.thumbnailUrl,
                    winnerName = gamePlay.winnerName,
                    comments = gamePlay.comments,
                )
            }
        }

    override suspend fun getGamePlayCount(): Int = dao.getGamePlayCount()

    companion object {
        private val gamePlayDateFormat = DateTimeFormatter.ISO_LOCAL_DATE
    }
}