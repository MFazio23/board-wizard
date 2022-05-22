package dev.mfazio.boardwizard.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.mfazio.boardwizard.data.entities.BoardGameEntity
import dev.mfazio.boardwizard.data.entities.GamePlayEntity
import dev.mfazio.boardwizard.data.entities.GamePlayWithGame

@Dao
interface BoardWizardDAO {
    @Query("SELECT * FROM board_games")
    suspend fun getAllBoardGames(): List<BoardGameEntity>

    @Query("SELECT * FROM board_games")
    fun getAllBoardGamesLiveData(): LiveData<List<BoardGameEntity>>

    @Query("SELECT * FROM board_games WHERE bggId = :id")
    fun getGameById(id: Int): LiveData<BoardGameEntity>

    @Query("SELECT COUNT(*) FROM board_games")
    suspend fun getBoardGameCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateGames(games: List<BoardGameEntity>): List<Long>

    @Query("SELECT * from game_plays")
    fun getAllGamePlays(): LiveData<List<GamePlayEntity>>

    @Transaction
    @Query("SELECT * from game_plays")
    fun getAllGamePlaysWithGames(): LiveData<List<GamePlayWithGame>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateGamePlays(games: List<GamePlayEntity>): List<Long>

    @Query("SELECT COUNT(*) FROM game_plays")
    suspend fun getGamePlayCount(): Int
}