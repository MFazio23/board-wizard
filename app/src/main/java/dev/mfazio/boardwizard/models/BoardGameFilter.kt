package dev.mfazio.boardwizard.models

sealed class BoardGameFilter(val filterFunction: (BoardGame) -> Boolean) {
    class MinimumAge(private val minAge: Int) : BoardGameFilter({ game ->
        game.communityMinAge == null || game.communityMinAge <= minAge
    })

    class PlayerCount(private val players: Int) : BoardGameFilter({ game ->
        ((game.minPlayers ?: 0)..(game.maxPlayers ?: Int.MAX_VALUE)).contains(players)
    })

    class FilteredWeights(private vararg val weights: Weights) : BoardGameFilter({ game ->
        weights.none() || weights.any { game.weight == null || it.isInWeight(game.weight) }
    })

    class FilteredPlayTimes(private vararg val playTimes: PlayTimes) : BoardGameFilter({ game ->
        playTimes.none() || playTimes.any { game.maxTime == null || it.isInLength(game.maxTime) }
    })


    enum class Weights(val start: Double, val end: Double) {
        Light(start = 0.0, end = 1.7),
        Medium(start = 1.7, end = 3.0),
        Heavy(start = 3.0, end = Double.MAX_VALUE);

        fun isInWeight(weightValue: Double) = (this.start..this.end).contains(weightValue)

    }

    enum class PlayTimes(val start: Int, val end: Int) {
        Short(start = 0, end = 30),
        Medium(start = 30, end = 90),
        Long(start = 90, end = Int.MAX_VALUE);

        fun isInLength(playTime: Int) = (this.start until this.end).contains(playTime)
    }
}
