package com.fadymarty.matule.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.catalog.CatalogRoot
import com.fadymarty.matule.presentation.home.HomeRoot
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.profile.ProfileRoot
import com.fadymarty.matule.presentation.projects.CreateProjectRoot
import com.fadymarty.matule_ui_kit.presentation.components.tab_bar.TabBar
import com.fadymarty.matule_ui_kit.presentation.components.tab_bar.TabBarItem

@Composable
fun MainScreen(
    rootNavController: NavHostController,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            TabBar(
                items = listOf(
                    TabBarItem(
                        icon = ImageVector.vectorResource(R.drawable.ic_home),
                        label = "Главная",
                        route = Route.Home
                    ),
                    TabBarItem(
                        icon = ImageVector.vectorResource(R.drawable.ic_catalog),
                        label = "Каталог",
                        route = Route.Catalog
                    ),
                    TabBarItem(
                        icon = ImageVector.vectorResource(R.drawable.ic_projects),
                        label = "Проекты",
                        iconSize = 24.dp,
                        spacing = 3.dp,
                        route = Route.Projects
                    ),
                    TabBarItem(
                        icon = ImageVector.vectorResource(R.drawable.ic_profile),
                        label = "Профиль",
                        route = Route.Profile
                    )
                ),
                selectedRoute = currentRoute,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
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
            startDestination = Route.Home
        ) {
            composable<Route.Home> {
                HomeRoot()
            }

            composable<Route.Catalog> {
                CatalogRoot(
                    rootNavController = rootNavController,
                    navController = navController
                )
            }

            navigation<Route.ProjectsGraph>(
                startDestination = Route.Projects
            ) {
                composable<Route.Projects> {

                }
                composable<Route.CreateProject> {
                    CreateProjectRoot()
                }
            }

            composable<Route.Profile> {
                ProfileRoot(
                    rootNavController = rootNavController
                )
            }
        }
    }
}