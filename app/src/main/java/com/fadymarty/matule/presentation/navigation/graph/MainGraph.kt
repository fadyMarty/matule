package com.fadymarty.matule.presentation.navigation.graph

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.AccentColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.presentation.cart.CartScreen
import com.fadymarty.matule.presentation.home.HomeScreen
import com.fadymarty.matule.presentation.navigation.screen.Screen

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val text: String
)

@Composable
fun MainGraph() {
    val navController = rememberNavController()

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_settings, text = "Home"),
            BottomNavigationItem(icon = R.drawable.ic_settings, text = "Search"),
            BottomNavigationItem(icon = R.drawable.ic_settings, text = "Bookmark"),
            BottomNavigationItem(icon = R.drawable.ic_settings, text = "Bookmark")
        )
    }

    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    selectedItem = remember(backStackState) {
        when (backStackState?.destination?.route) {
            Screen.Home.route -> 0
            else -> 0
        }
    }
    val isBottomBarVisible = remember(backStackState) {
        backStackState?.destination?.route == Screen.Home.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar(
                    containerColor = Color.Red
                ) {
                    bottomNavigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_settings),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = item.text
                                )
                            },
                            selected = selectedItem == index,
                            onClick = { }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                shape = CircleShape,
                modifier = Modifier
                    .offset(y = 50.dp)
                    .size(56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = AccentColor,
                        spotColor = AccentColor

                    ),
                containerColor = AccentColor
            ) {
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            route = Graph.MAIN,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    navController = navController
                )
            }
        }
    }
}

private fun navigateToTap(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}

@Preview
@Composable
fun MainGraphPreview() {
    MatuleTheme {
        MainGraph()
    }
}