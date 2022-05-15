package dev.mfazio.boardwizard.ui.screens.gameplays

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.mfazio.boardwizard.models.GamePlay

@Composable
fun GamePlaysScreen(
    viewModel: GamePlaysViewModel = hiltViewModel()
) {
    val gamePlays by viewModel.gamePlays.observeAsState()
    val isRefreshing = viewModel.isRefreshing
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { viewModel.refreshGamePlayList() }) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(
                    items = gamePlays ?: emptyList(),
                    key = { gamePlay -> gamePlay.gamePlayId }
                ) { gamePlay ->
                    GamePlaysScreenItem(gamePlay = gamePlay)
                }
            }
        }
    }
}

@Composable
fun GamePlaysScreenItem(gamePlay: GamePlay) {
    val uriHandler = LocalUriHandler.current
    Row(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { openBGGPage(gamePlay.gamePlayId, uriHandler) }) {
        AsyncImage(
            model = gamePlay.thumbnailUrl,
            contentDescription = "${gamePlay.gameName} thumbnail image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .padding(4.dp)
                .size(48.dp),
        )
        Column {
            Text(text = gamePlay.gameName, fontSize = 18.sp)
            Row {
                Column(modifier = Modifier.padding(4.dp)) {
                    Text(text = gamePlay.playDate.toString(), fontSize = 14.sp)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = "Winner: ${gamePlay.winnerName ?: "N/A"}", fontSize = 16.sp)
                    Text(
                        text = gamePlay.comments ?: "",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(text = gamePlay.location, fontSize = 12.sp)
                }
            }
        }
    }
    Divider(color = Color.Black, thickness = 1.dp)
}

private const val bggBaseUrl = "https://boardgamegeek.com/play/details/"

private fun openBGGPage(gamePlayId: Long, uriHandler: UriHandler) {
    uriHandler.openUri(
        "$bggBaseUrl/$gamePlayId"
    )
}