@file:OptIn(ExperimentalMaterialApi::class)

package dev.mfazio.boardwizard.ui.screens.randomizer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.ui.animation.LottieAnimationHelper
import dev.mfazio.boardwizard.ui.components.filter.BoardGameFilterSettings
import dev.mfazio.boardwizard.ui.components.filter.BottomFilterSheet
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun Randomizer(
    boardGameFilterSettings: BoardGameFilterSettings,
    onFiltersUpdated: (BoardGameFilterSettings) -> Unit,
    viewModel: RandomizerViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var didCheckForGame by remember { mutableStateOf(false) }
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

    viewModel.boardGameFilterSettings = boardGameFilterSettings

    val showFilterDrawer = {
        scope.launch {
            showFABs = false
            scaffoldState.bottomSheetState.expand()
        }
    }

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
                            viewModel.clearCurrentGame()
                            isLoading = true
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
                        onClick = { showFilterDrawer() }
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
                    startingSettings = boardGameFilterSettings,
                    onFilter = {
                        onFiltersUpdated(it)
                        closeFilterDrawer()
                        viewModel.clearCurrentGame()
                        isLoading = true
                    },
                    onCloseIconTapped = { closeFilterDrawer() }
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                RandomizerMain(
                    boardGame = viewModel.currentGame,
                    isLoading = isLoading,
                    didCheckForGame = didCheckForGame,
                    modifier = Modifier.weight(weight = 1F, fill = true),
                    onStartSearching = {
                        viewModel.clearCurrentGame()
                        isLoading = true
                    },
                    onLoadingComplete = {
                        isLoading = false
                        didCheckForGame = true
                        viewModel.getRandomGame()
                    },
                    showFilterDrawer = { showFilterDrawer() }
                )
            }
        }
    }
}

@Composable
fun RandomizerMain(
    boardGame: BoardGame?,
    isLoading: Boolean,
    didCheckForGame: Boolean,
    modifier: Modifier = Modifier,
    onStartSearching: () -> Unit,
    onLoadingComplete: () -> Unit,
    showFilterDrawer: () -> Unit,
) {
    Box(modifier = modifier) {
        when {
            boardGame?.title != null -> GameInfo(boardGame)
            isLoading -> GameLoading(onLoadingComplete)
            !didCheckForGame -> NoGameYet(onStartSearching)
            else -> NoGameFound { showFilterDrawer() }
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
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_70218_board_game)
    )
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

    if (progress == 1F) {
        onLoadingComplete()
    }
}

@Composable
fun GameInfo(boardGame: BoardGame) {
    val gameTime = when {
        boardGame.minTime == boardGame.maxTime -> "${boardGame.minTime.toString()} min."
        boardGame.maxTime == null || boardGame.maxTime > 240 -> "${boardGame.minTime}+ min."
        else -> "${boardGame.minTime} - ${boardGame.maxTime} min."
    }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = boardGame.title ?: "",
            fontSize = 32.sp,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        AsyncImage(
            model = boardGame.imageUrl,
            contentDescription = "${boardGame.title} image",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .size(240.dp),
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = "Players: ${boardGame.minPlayers} - ${boardGame.maxPlayers}",
                    fontSize = 18.sp
                )
                Text(
                    text = "Time: $gameTime",
                    fontSize = 18.sp
                )
            }
            Column(modifier = Modifier.weight(1F)) {
                Text(text = "Age: ${boardGame.communityMinAge}+", fontSize = 18.sp)
                Text(
                    text = "Weight: ${DecimalFormat("#.00").format(boardGame.weight)}/5",
                    fontSize = 18.sp
                )
            }
        }
    }

}

@Composable
fun NoGameFound(onEditFilters: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No game found.",
            fontSize = 32.sp
        )
        Button(onClick = onEditFilters, modifier = Modifier.padding(top = 8.dp)) {
            Text("Update Filters")
        }
    }
}

@Preview
@Composable
fun GameInfoPreview() {
    val boardGame = BoardGame(
        bggId = 266192,
        title = "Wingspan",
        minPlayers = 1,
        maxPlayers = 5,
        minTime = 40,
        maxTime = 70,
        officialMinAge = 10,
        communityMinAge = 10,
        weight = 2.4442,
        imageUrl = "https://cf.geekdo-images.com/yLZJCVLlIx4c7eJEWUNJ7w__original/img/cI782Zis9cT66j2MjSHKJGnFPNw=/0x0/filters:format(jpeg)/pic4458123.jpg",
        thumbnailUrl = "https://cf.geekdo-images.com/yLZJCVLlIx4c7eJEWUNJ7w__thumb/img/VNToqgS2-pOGU6MuvIkMPKn_y-s=/fit-in/200x150/filters:strip_icc()/pic4458123.jpg",
    )

    GameInfo(boardGame = boardGame)
}