package dev.mfazio.boardwizard.ui.screens.randomizer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.ui.animation.LottieAnimationHelper

@Composable
fun Randomizer(
    gameName: String?,
    onClearGame: () -> Unit,
    viewModel: RandomizerViewModel = viewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_filter_list_24),
                    contentDescription = "Game randomizer filter"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            RandomizerMain(
                game = gameName,
                isLoading = isLoading,
                modifier = Modifier.weight(weight = 1F, fill = true),
                onStartSearching = {
                    onClearGame()
                    isLoading = true
                },
                onLoadingComplete = {
                    isLoading = false
                }
            )
        }
    }
}

@Composable
fun RandomizerMain(
    game: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onStartSearching: () -> Unit,
    onLoadingComplete: () -> Unit,
) {
    Box(modifier = modifier) {
        when {
            game != null -> GameInfo(onStartSearching)
            isLoading -> GameLoading(onLoadingComplete)
            else -> NoGameYet(onStartSearching)
        }
    }
}

@Composable
fun NoGameYet(onBoardTapped: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_70218_board_game))
    val progress by animateLottieCompositionAsState(
        composition,
        clipSpec = LottieClipSpec.Frame(137, 138)
    )

    val dynamicProperties = LottieAnimationHelper.getBoardGameProperties()

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier
            .offset(y = (-40).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onBoardTapped()
            },
        dynamicProperties = dynamicProperties,
    )
}

@Composable
fun GameLoading(onLoadingComplete: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_70218_board_game))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        speed = 2F,
    )

    val dynamicProperties = LottieAnimationHelper.getBoardGameProperties()

    /*if (progress >= 0.99F) {
        onLoadingComplete()
    }*/

    LottieAnimation(
        composition = composition,
        progress = progress,
        dynamicProperties = dynamicProperties,
        modifier = Modifier.offset(y = (-40).dp),
    )
}

@Composable
fun GameInfo(onStartSearching: () -> Unit) {
    Column {
        Text("Game info")
        Button(onClick = onStartSearching) {
            Text("Try again?")
        }
    }
}

@Composable
fun NoGameFound(onEditFilters: () -> Unit) {
    Column {
        Text("Nothing found.")
        Button(onClick = onEditFilters) {
            Text("Try again?")
        }
    }
}

@Preview
@Composable
fun GameInfoPreview() {
    GameInfo {}
}