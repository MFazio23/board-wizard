package dev.mfazio.boardwizard.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mfazio.boardwizard.R
import dev.mfazio.boardwizard.ui.components.filter.BoardGameFilterSettings
import dev.mfazio.boardwizard.ui.navigation.BoardWizardNavScreen
import dev.mfazio.boardwizard.ui.screens.gamelist.GameListScreen
import dev.mfazio.boardwizard.ui.screens.gameplays.GamePlaysScreen
import dev.mfazio.boardwizard.ui.screens.randomizer.Randomizer
import dev.mfazio.boardwizard.ui.theme.BoardWizardTheme
import dev.mfazio.boardwizard.ui.theme.PlayFontFamily
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardWizardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BoardWizardRoot(mainViewModel)
                }
            }
        }

        mainViewModel.updateGamesIfNecessary()
    }
}

@Composable
fun BoardWizardRoot(
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    // I added the default username to distinguish between the starting state and a missing name.
    val defaultUserName = "N/A"

    val userName by mainViewModel.userName.observeAsState(defaultUserName)

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    var boardGameFilterSettings by remember { mutableStateOf(BoardGameFilterSettings.default) }

    mainViewModel.snackBarMessage?.let { snackbarMessage ->
        scope.launch {
            snackbarHostState.showSnackbar(snackbarMessage)
        }
    }

    Scaffold(
        topBar = { BoardWizardTopBar(navController) },
        bottomBar = { BoardWizardBottomNav(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = innerPadding.calculateBottomPadding(),
            )
        ) {
            BoardWizardNavHost(
                navController = navController,
                boardGameFilterSettings = boardGameFilterSettings,
                onFiltersUpdated = { boardGameFilterSettings = it }
            )
            if (userName.isNullOrEmpty()) {
                var bggUserNameInput by remember { mutableStateOf("") }
                BoardWizardBGGNameDialog(
                    userName = bggUserNameInput,
                    onUpdateUserName = { userName -> bggUserNameInput = userName },
                    onConfirmUserName = {
                        Timber.tag("Board Wizard").i("BGGUserName = $bggUserNameInput")
                        if (bggUserNameInput.isNotEmpty()) {
                            mainViewModel.updateUserName(bggUserNameInput)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BoardWizardBGGNameDialog(
    userName: String,
    onUpdateUserName: (String) -> Unit,
    onConfirmUserName: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.bgg_username_required))
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.bgg_username_info))
                TextField(
                    modifier = Modifier.padding(top = 8.dp),
                    value = userName,
                    label = { Text(stringResource(id = R.string.bgg_username)) },
                    onValueChange = onUpdateUserName
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onConfirmUserName) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    )
}

@Composable
fun BoardWizardTopBar(navController: NavHostController) {
    TopAppBar(
        title = { BoardWizardTitle() },
        backgroundColor = Color.Transparent,
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = {

            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings icon")
            }
        },
        elevation = 0.dp,
    )
}

@Composable
fun BoardWizardTitle() {
    Text(
        text = "Board Wizard",
        fontFamily = PlayFontFamily,
    )
}

@Composable
fun BoardWizardBottomNav(navController: NavHostController) {
    val bottomNavScreens = listOf(
        BoardWizardNavScreen.GameList,
        BoardWizardNavScreen.Randomizer,
        BoardWizardNavScreen.GamePlays,
    )
    BottomAppBar(
        modifier = Modifier
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)),
        elevation = 16.dp,
    ) {
        BottomNavigation(
            modifier = Modifier.clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            bottomNavScreens.forEach { screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val label = stringResource(screen.labelRes)
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(screen.iconRes),
                            contentDescription = screen.route
                        )
                    },
                    label = { Text(label) },
                    selected = isSelected,
                    alwaysShowLabel = false,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BoardWizardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    boardGameFilterSettings: BoardGameFilterSettings,
    onFiltersUpdated: (BoardGameFilterSettings) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = BoardWizardNavScreen.Randomizer.route,
        modifier = modifier,
    ) {
        composable(BoardWizardNavScreen.GameList.route) {
            GameListScreen(
                boardGameFilterSettings = boardGameFilterSettings,
                onFiltersUpdated = onFiltersUpdated,
            )
        }
        composable(BoardWizardNavScreen.Randomizer.route) {
            Randomizer(
                boardGameFilterSettings = boardGameFilterSettings,
                onFiltersUpdated = onFiltersUpdated,
            )
        }
        composable(BoardWizardNavScreen.GamePlays.route) {
            GamePlaysScreen()
        }
    }
}