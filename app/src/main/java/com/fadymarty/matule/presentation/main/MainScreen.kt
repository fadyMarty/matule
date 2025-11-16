package com.fadymarty.matule.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.fadymarty.matule.presentation.home.HomeScreen
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.profile.ProfileScreen
import com.fadymarty.matule.presentation.projects.CreateProjectScreen
import com.fadymarty.matule_ui_kit.presentation.components.icons.MatuleIcons
import com.fadymarty.matule_ui_kit.presentation.components.tab_bar.TabBar
import com.fadymarty.matule_ui_kit.presentation.components.tab_bar.TabBarItem

@Composable
fun MainScreen(
    rootNavController: NavHostController,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            TabBar(
                items = listOf(
                    TabBarItem(
                        icon = MatuleIcons.Home,
                        label = "Главная",
                        route = Route.HomeScreen
                    ),
                    TabBarItem(
                        icon = MatuleIcons.Catalog,
                        label = "Каталог",
                        route = Route.CatalogScreen
                    ),
                    TabBarItem(
                        icon = MatuleIcons.Projects,
                        label = "Проекты",
                        iconSize = 24.dp,
                        spacing = 3.dp,
                        route = Route.ProjectsNavigation
                    ),
                    TabBarItem(
                        icon = MatuleIcons.Profile,
                        label = "Профиль",
                        route = Route.ProfileScreen
                    )
                ),
                selectedRoute = selectedRoute,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding()
            ),
            navController = navController,
            startDestination = Route.HomeScreen
        ) {
            composable<Route.HomeScreen> {
                HomeScreen()
            }

            composable<Route.CatalogScreen> {

            }

            navigation<Route.ProjectsNavigation>(
                startDestination = Route.ProjectsScreen
            ) {
                composable<Route.ProjectsScreen> {

                }
                composable<Route.CreateProjectScreen> {
                    CreateProjectScreen()
                }
            }

            composable<Route.ProfileScreen> {
                ProfileScreen(
                    rootNavController = rootNavController
                )
            }
        }
    }
}