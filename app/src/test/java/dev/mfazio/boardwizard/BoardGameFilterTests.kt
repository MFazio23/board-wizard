package dev.mfazio.boardwizard

import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.models.BoardGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardGameFilterTests {
    @Test
    fun `Empty list of filters return all games`() {
        val filteredGames = testGames

        assertEquals(96, filteredGames.size)
    }

    @Test
    fun `Age filter of 4 returns fewer games`() {
        val filteredGames = filterGames(
            testGames,
            listOf(BoardGameFilter.YoungestPlayer(4))
        )

        assertEquals(7, filteredGames.size)
    }

    @Test
    fun `Age filter of 10 returns fewer games`() {
        val filteredGames = filterGames(
            testGames,
            listOf(BoardGameFilter.YoungestPlayer(10))
        )

        assertEquals(89, filteredGames.size)
    }

    @Test
    fun `Four players returns games that can support that many people`() {
        val filteredGames = filterGames(
            testGames,
            listOf(BoardGameFilter.PlayerCount(4))
        )

        assertEquals(88, filteredGames.size)
    }

    @Test
    fun `Only including light weight games works as expected`() {
        val filteredGames = filterGames(
            testGames,
            listOf(BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Light))
        )

        assertEquals(54, filteredGames.size)
    }

    @Test
    fun `Only including light and heavy weight games works as expected`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Light,
                    BoardGameFilter.Weights.Heavy,
                )
            )
        )

        assertEquals(55, filteredGames.size)
    }

    @Test
    fun `Only including long games works as expected`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Long
                )
            )
        )

        assertEquals(6, filteredGames.size)
    }

    @Test
    fun `Medium length, medium games work correctly`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium
                )
            )
        )

        assertEquals(33, filteredGames.size)
    }

    @Test
    fun `Light weight games for five+ players`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.PlayerCount(5),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Light
                )
            )
        )

        assertEquals(29, filteredGames.size)
    }

    @Test
    fun `Medium weight games for three+ players`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.PlayerCount(3),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium
                )
            )
        )

        assertEquals(39, filteredGames.size)
    }

    @Test
    fun `Long games for five+ players`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.PlayerCount(5),
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Long
                )
            )
        )

        assertEquals(5, filteredGames.size)
    }

    @Test
    fun `Medium weight games for 10+`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Medium),
                BoardGameFilter.YoungestPlayer(10)
            )
        )

        assertEquals(38, filteredGames.size)
    }

    @Test
    fun `Medium length games for 6+`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Medium),
                BoardGameFilter.YoungestPlayer(6)
            )
        )

        assertEquals(11, filteredGames.size)
    }

    @Test
    fun `Medium weight and length games for 5 players`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Medium),
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Medium),
                BoardGameFilter.PlayerCount(5),
            )
        )

        assertEquals(18, filteredGames.size)
    }

    @Test
    fun `Medium weight and length games for 8 year olds and up`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Medium),
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Medium),
                BoardGameFilter.YoungestPlayer(8),
            )
        )

        assertEquals(21, filteredGames.size)
    }

    @Test
    fun `Medium weight and length games for six year olds`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Medium),
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Medium),
                BoardGameFilter.YoungestPlayer(6),
            )
        )

        assertEquals(3, filteredGames.size)
    }

    @Test
    fun `Quick, easy games for 8+ players`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Short),
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Light),
                BoardGameFilter.PlayerCount(8),
            )
        )

        assertEquals(9, filteredGames.size)
    }

    @Test
    fun `Quick, easy games for 6+ players with min age of eight`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(BoardGameFilter.PlayTimes.Short),
                BoardGameFilter.FilteredWeights(BoardGameFilter.Weights.Light),
                BoardGameFilter.YoungestPlayer(8),
                BoardGameFilter.PlayerCount(6),
            )
        )

        assertEquals(7, filteredGames.size)
    }

    @Test
    fun `Medium or long games`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
            )
        )

        assertEquals(63, filteredGames.size)
    }

    @Test
    fun `Medium or long games with medium weight`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium
                )
            )
        )

        assertEquals(38, filteredGames.size)
    }

    @Test
    fun `Medium or long games with medium or heavy weight`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium,
                    BoardGameFilter.Weights.Heavy,
                )
            )
        )

        assertEquals(39, filteredGames.size)
    }

    @Test
    fun `Medium or long games with medium or heavy weight for ages 12+`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium,
                    BoardGameFilter.Weights.Heavy,
                ),
                BoardGameFilter.YoungestPlayer(12),
            )
        )

        assertEquals(39, filteredGames.size)
    }

    @Test
    fun `Medium or long games with medium or heavy weight for 5+ players 10 and up`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Medium,
                    BoardGameFilter.Weights.Heavy,
                ),
                BoardGameFilter.YoungestPlayer(10),
                BoardGameFilter.PlayerCount(5),
            )
        )

        assertEquals(20, filteredGames.size)
    }

    @Test
    fun `Long games with heavy weight for players 6 and up returns nothing`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Heavy,
                ),
                BoardGameFilter.YoungestPlayer(6),
            )
        )

        assertTrue(filteredGames.isEmpty())
    }

    @Test
    fun `All the weight and time filters return all the games`() {
        val filteredGames = filterGames(
            testGames,
            listOf(
                BoardGameFilter.FilteredPlayTimes(
                    BoardGameFilter.PlayTimes.Short,
                    BoardGameFilter.PlayTimes.Medium,
                    BoardGameFilter.PlayTimes.Long
                ),
                BoardGameFilter.FilteredWeights(
                    BoardGameFilter.Weights.Light,
                    BoardGameFilter.Weights.Medium,
                    BoardGameFilter.Weights.Heavy,
                ),
            )
        )

        assertEquals(96, filteredGames.count())
    }

    fun filterGames(games: List<BoardGame>, filters: List<BoardGameFilter>): List<BoardGame> =
        games.filter { game ->
            filters.all {
                it.filterFunction(game)
            }
        }

    companion object {
        private var testGames: List<BoardGame> = BoardGameTestData.testGames
    }
}