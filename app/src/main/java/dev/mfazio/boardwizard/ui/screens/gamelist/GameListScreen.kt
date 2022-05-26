@file:OptIn(ExperimentalMaterialApi::class)

package dev.mfazio.boardwizard.ui.screens.gamelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.models.BoardGame
import dev.mfazio.boardwizard.ui.components.filter.BoardGameFilterSettings
import dev.mfazio.boardwizard.ui.components.filter.BottomFilterSheet
import kotlinx.coroutines.launch

@Composable
fun GameListScreen(
    boardGameFilterSettings: BoardGameFilterSettings,
    onFiltersUpdated: (BoardGameFilterSettings) -> Unit,
    viewModel: GameListViewModel = hiltViewModel()
) {
    val games by viewModel.filteredGames.observeAsState()
    val isRefreshing = viewModel.isRefreshing
    var showFAB by remember { mutableStateOf(true) }

    viewModel.boardGameFilterSettings.value = boardGameFilterSettings

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { stateValue ->
                showFAB = stateValue == BottomSheetValue.Collapsed
                true
            }
        )
    )

    val showFilterDrawer = {
        scope.launch {
            showFAB = false
            scaffoldState.bottomSheetState.expand()
        }
    }

    val closeFilterDrawer = {
        scope.launch {
            showFAB = true
            scaffoldState.bottomSheetState.collapse()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (showFAB) {
                Column {
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
    ) { paddingValues ->
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                BottomFilterSheet(
                    startingSettings = boardGameFilterSettings,
                    onFilter = {
                        onFiltersUpdated(it)
                        closeFilterDrawer()
                    },
                    onCloseIconTapped = { closeFilterDrawer() }
                )
            },
        ) {
            SwipeRefresh(
                modifier = Modifier.padding(paddingValues),
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = { viewModel.refreshGameList() }) {
                LazyColumn {
                    items(
                        items = games ?: emptyList(),
                        key = { game -> game.bggId }
                    ) { game ->
                        GameListScreenItem(game = game)
                    }
                }
            }
        }
    }

}

@Composable
fun GameListScreenItem(game: BoardGame) {
    val uriHandler = LocalUriHandler.current
    Row(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { openBGGPage(game.bggId, uriHandler) }) {
        AsyncImage(
            model = game.thumbnailUrl,
            contentDescription = "${game.title} thumbnail image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .clip(CircleShape)
                .padding(4.dp)
                .size(48.dp),
        )
        Column {
            Text(text = game.title ?: "N/A", fontSize = 20.sp)
        }
    }
    Divider(color = Color.Black, thickness = 1.dp)
}

private const val bggBaseUrl = "https://boardgamegeek.com/boardgame/"

private fun openBGGPage(gameId: Long, uriHandler: UriHandler) {
    uriHandler.openUri("$bggBaseUrl/$gameId")
}