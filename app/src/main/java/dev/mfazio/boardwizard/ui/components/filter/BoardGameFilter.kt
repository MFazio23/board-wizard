@file:OptIn(ExperimentalMaterialApi::class)

package dev.mfazio.boardwizard.ui.screens.randomizer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mfazio.boardwizard.models.BoardGameFilter
import dev.mfazio.boardwizard.ui.theme.FilterGreen
import dev.mfazio.boardwizard.ui.theme.FilterRed
import dev.mfazio.boardwizard.ui.theme.FilterYellow
import kotlin.math.round

@Composable
fun BottomFilterSheet(
    startingSettings: BoardGameFilterSettings,
    onFilter: (BoardGameFilterSettings) -> Unit,
    onCloseIconTapped: () -> Unit
) {
    val textColor = MaterialTheme.colors.onSurface

    var players by remember { mutableStateOf(startingSettings.players.toFloat()) }
    var minimumAge by remember { mutableStateOf(startingSettings.minimumAge.toFloat()) }

    var weightChips by remember {
        mutableStateOf(
            listOf(
                FilterChipItem(
                    value = BoardGameFilter.Weights.Light,
                    text = "Light",
                    selectedBackgroundColor = FilterGreen,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.weights.contains(
                        BoardGameFilter.Weights.Light
                    ),
                ),
                FilterChipItem(
                    value = BoardGameFilter.Weights.Medium,
                    text = "Medium",
                    selectedBackgroundColor = FilterYellow,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.weights.contains(
                        BoardGameFilter.Weights.Medium
                    ),
                ),
                FilterChipItem(
                    value = BoardGameFilter.Weights.Heavy,
                    text = "Heavy",
                    selectedBackgroundColor = FilterRed,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.weights.contains(
                        BoardGameFilter.Weights.Heavy
                    ),
                ),
            )
        )
    }
    var playTimeChips by remember {
        mutableStateOf(
            listOf(
                FilterChipItem(
                    value = BoardGameFilter.PlayTimes.Short,
                    text = "Short",
                    selectedBackgroundColor = FilterGreen,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.playingTimes.contains(
                        BoardGameFilter.PlayTimes.Short
                    ),
                ),
                FilterChipItem(
                    value = BoardGameFilter.PlayTimes.Medium,
                    text = "Medium",
                    selectedBackgroundColor = FilterYellow,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.playingTimes.contains(
                        BoardGameFilter.PlayTimes.Medium
                    ),
                ),
                FilterChipItem(
                    value = BoardGameFilter.PlayTimes.Long,
                    text = "Long",
                    selectedBackgroundColor = FilterRed,
                    selectedContentColor = textColor,
                    isSelected = startingSettings.playingTimes.contains(
                        BoardGameFilter.PlayTimes.Long
                    ),
                ),
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        BottomFilterSheetHeader(onCloseIconTapped)
        BottomFilterSheetList(
            players = players,
            minimumAge = minimumAge,
            weightChips = weightChips,
            playTimeChips = playTimeChips,
            playersUpdated = { players = round(it) },
            minimumAgeUpdated = { minimumAge = round(it) },
            weightChipTapped = { tappedChip ->
                weightChips = weightChips.map { chip ->
                    if (tappedChip.value == chip.value) {
                        chip.copy(isSelected = !chip.isSelected)
                    } else chip
                }
            },
            playTimeChipTapped = { tappedChip ->
                playTimeChips = playTimeChips.map { chip ->
                    if (tappedChip.value == chip.value) {
                        chip.copy(isSelected = !chip.isSelected)
                    } else chip
                }
            }
        )
        BottomFilterSheetButtons(
            onReset = {
                players = startingSettings.players.toFloat()
                minimumAge = startingSettings.minimumAge.toFloat()
                weightChips = weightChips.map { chip ->
                    chip.copy(isSelected = startingSettings.weights.contains(chip.value))
                }
                playTimeChips = playTimeChips.map { chip ->
                    chip.copy(isSelected = startingSettings.playingTimes.contains(chip.value))
                }
            },
            onFilter = {
                onFilter(
                    BoardGameFilterSettings(
                        players = players.toInt(),
                        minimumAge = minimumAge.toInt(),
                        weights = weightChips.filter { it.isSelected }.map { it.value },
                        playingTimes = playTimeChips.filter { it.isSelected }.map { it.value },
                    )
                )
            }
        )
    }
}

@Composable
fun BottomFilterSheetHeader(onCloseIconTapped: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        ) {
            IconButton(onClick = onCloseIconTapped) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
        Text(
            text = "Filter Games",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun BottomFilterSheetList(
    players: Float,
    minimumAge: Float,
    weightChips: List<FilterChipItem<BoardGameFilter.Weights>>,
    playTimeChips: List<FilterChipItem<BoardGameFilter.PlayTimes>>,
    playersUpdated: (Float) -> Unit,
    minimumAgeUpdated: (Float) -> Unit,
    weightChipTapped: (FilterChipItem<BoardGameFilter.Weights>) -> Unit,
    playTimeChipTapped: (FilterChipItem<BoardGameFilter.PlayTimes>) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        BottomFilterSlider(
            labelText = "Players",
            sliderValue = players,
            onSliderValueChange = playersUpdated,
            sliderValueRange = 1F..10F
        )
        BottomFilterSlider(
            labelText = "Minimum Age",
            sliderValue = minimumAge,
            onSliderValueChange = minimumAgeUpdated,
            sliderValueRange = 2F..18F
        )
        BottomFilterChips(
            labelText = "Weight/Complexity",
            filterValues = weightChips,
            onChipTapped = weightChipTapped,
        )
        BottomFilterChips(
            labelText = "Playing Time",
            filterValues = playTimeChips,
            onChipTapped = playTimeChipTapped,
        )
    }
}

@Composable
fun BottomFilterSlider(
    labelText: String,
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    sliderValueRange: ClosedFloatingPointRange<Float>
) {
    val sliderValueText = if (sliderValue == sliderValueRange.endInclusive) {
        "${sliderValue.toInt()}+"
    } else sliderValue.toInt().toString()
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "$labelText - $sliderValueText")
        Slider(
            value = sliderValue,
            onValueChange = onSliderValueChange,
            valueRange = sliderValueRange,
        )
    }
}

@Composable
fun <T> BottomFilterChips(
    labelText: String,
    filterValues: List<FilterChipItem<T>>,
    onChipTapped: (FilterChipItem<T>) -> Unit,
) {
    val isLightTheme = MaterialTheme.colors.isLight

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = labelText)
        LazyRow {
            items(filterValues) { filterValue ->
                FilterChip(
                    onClick = {
                        println("Chip tapped$filterValue")
                        onChipTapped(filterValue)
                    },
                    selected = filterValue.isSelected,
                    modifier = Modifier.padding(8.dp),
                    colors = ChipDefaults.filterChipColors(
                        backgroundColor = if (isLightTheme) Color.Gray else Color.DarkGray,
                        contentColor = filterValue.selectedBackgroundColor,
                        selectedBackgroundColor = filterValue.selectedBackgroundColor,
                        selectedContentColor = filterValue.selectedContentColor,
                    )
                ) {
                    Text(filterValue.text, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun BottomFilterSheetButtons(
    onReset: () -> Unit,
    onFilter: () -> Unit,
) {
    Row(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Gray,
                contentColor = MaterialTheme.colors.onPrimary,
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text("Reset")
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onFilter,
                modifier = Modifier.align(Alignment.CenterEnd),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                )
            ) {
                Text(text = "Filter", fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun BottomFilterSliderPreview() {
    BottomFilterSlider(
        labelText = "Players",
        sliderValue = 5F,
        onSliderValueChange = { },
        sliderValueRange = 1F..10F
    )
}

@Preview
@Composable
fun BottomFilterChipsPreview() {
    BottomFilterChips<Any?>(
        labelText = "Players",
        filterValues = listOf(
            FilterChipItem(
                1F,
                "ItemA",
                selectedBackgroundColor = FilterGreen,
                selectedContentColor = Color.Black,
                false
            ),
            FilterChipItem(
                2F,
                "ItemB",
                selectedBackgroundColor = FilterRed,
                selectedContentColor = Color.Black,
                true
            )
        ),
        {}
    )
}