@file:OptIn(ExperimentalMaterialApi::class)

package dev.mfazio.boardwizard.ui.screens.randomizer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.ui.animation.LottieAnimationHelper
import kotlinx.coroutines.launch

@Composable
fun Randomizer(
    onClearGame: () -> Unit,
    viewModel: RandomizerViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var showFABs by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { stateValue ->
                showFABs = stateValue == BottomSheetValue.Collapsed
                true
            }
        )
    )

    val closeFilterDrawer = {
        scope.launch {
            showFABs = true
            scaffoldState.bottomSheetState.collapse()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (showFABs) {
                Column {
                    FloatingActionButton(
                        onClick = {
                            viewModel.getRandomGame()
                        },
                        modifier = Modifier.padding(bottom = 16.dp),
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_casino_24),
                            contentDescription = "Get random game"
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                showFABs = false
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_filter_list_24),
                            contentDescription = "Game randomizer filter"
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                BottomFilterSheet(
                    startingSettings = viewModel.boardGameFilterSettings,
                    onFilter = {
                        viewModel.boardGameFilterSettings = it
                        closeFilterDrawer()
                        viewModel.getRandomGame()
                    },
                    onCloseIconTapped = {
                        closeFilterDrawer()
                    }
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                Text(viewModel.boardGameFilterSettings.toString())
                RandomizerMain(
                    boardGame = viewModel.currentGame,
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
}

@Composable
fun RandomizerMain(
    boardGame: BoardGame?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onStartSearching: () -> Unit,
    onLoadingComplete: () -> Unit,
) {
    Box(modifier = modifier) {
        when {
            boardGame != null -> GameInfo(boardGame)
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

    LottieAnimation(
        composition = composition,
        progress = progress,
        dynamicProperties = dynamicProperties,
        modifier = Modifier.offset(y = (-40).dp),
    )
}

@Composable
fun GameInfo(boardGame: BoardGame?) {
    Column {
        Text(boardGame?.title ?: "No game", fontSize = 28.sp)
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