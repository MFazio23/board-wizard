package dev.mfazio.boardwizard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.mfazio.boardwizard.ui.Randomizer
import dev.mfazio.boardwizard.ui.theme.BoardWizardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardWizardTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background) {
                    BoardWizardRoot()
                }
            }
        }
    }
}

@Composable
fun BoardWizardRoot() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BoardWizardBottomNav(navController) }
    ) {
        BoardWizardNavHost(navController = navController)
    }
}

@Composable
fun BoardWizardBottomNav(navController: NavHostController) {
    val bottomNavScreens = listOf(
        BoardWizardNavScreen.GameList,
        BoardWizardNavScreen.Randomizer,
        BoardWizardNavScreen.PlayerList,
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavScreens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
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
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }

                        launchSingleTop = true

                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun BoardWizardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BoardWizardNavScreen.Randomizer.route,
        modifier = modifier
    ) {
        composable(BoardWizardNavScreen.GameList.route) {
            Text("Game List")
        }
        composable(BoardWizardNavScreen.Randomizer.route) {
            Randomizer()
        }
        composable(BoardWizardNavScreen.PlayerList.route) {
            Text("Player List")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BoardWizardTheme {

    }
}