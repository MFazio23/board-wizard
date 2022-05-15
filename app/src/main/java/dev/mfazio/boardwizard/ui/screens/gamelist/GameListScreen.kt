package dev.mfazio.boardwizard.ui.screens.gamelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.mfazio.boardwizard.models.BoardGame

@Composable
fun GameListScreen(viewModel: GameListViewModel = hiltViewModel()) {
    val games by viewModel.games.observeAsState()
    val isRefreshing = viewModel.isRefreshing
    SwipeRefresh(
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