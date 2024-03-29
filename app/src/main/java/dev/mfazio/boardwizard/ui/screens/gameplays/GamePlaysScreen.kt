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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.mfazio.boardwizard.models.GamePlay
import java.time.LocalDate

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
            Row {
                Text(
                    text = gamePlay.gameName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1F)
                )
                Box {
                    Text(
                        text = gamePlay.playDate.toString(),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Row {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1F)
                ) {
                    Text(
                        text = "Winner: ${gamePlay.winnerName ?: "N/A"}",
                        fontSize = 16.sp,
                        modifier = Modifier
                    )
                    Box(modifier = Modifier) {
                        Text(
                            text = "Location: ${gamePlay.location}",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .align(Alignment.BottomStart)
                        )
                    }
                }
                Text(
                    text = gamePlay.comments ?: "",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
    Divider(color = Color.Black, thickness = 1.dp)
}

@Preview
@Composable
fun GamePlaysScreenItemPreview() {
    GamePlaysScreenItem(gamePlay = GamePlay(
        gamePlayId = 1,
        gameName = "My City",
        playDate = LocalDate.now(),
        location = "F.",
        thumbnailUrl = "https://cf.geekdo-images.com/vjLg-uWRx3SICFZQehHqkA__thumb/img/-0dgIVp3J1dIi1dD7WddEfEctzw=/fit-in/200x150/filters:strip_icc()/pic5428585.jpg",
        winnerName = "Michael",
        comments = """
            Episode 24
            
            Final scoring:
            Tim - 54
            Emily - 42
            Michael - 37
        """.trimIndent()
    ))
}

private const val bggBaseUrl = "https://boardgamegeek.com/play/details/"

private fun openBGGPage(gamePlayId: Long, uriHandler: UriHandler) {
    uriHandler.openUri(
        "$bggBaseUrl/$gamePlayId"
    )
}