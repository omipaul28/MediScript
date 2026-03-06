package com.nacoders.mediscript.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {

    NavigationBar {

        val backStackEntry =
            navController.currentBackStackEntryAsState()

        val currentRoute =
            backStackEntry.value?.destination?.route

        bottomNavItems.forEach { item ->

            NavigationBarItem(

                selected = currentRoute == item.route,

                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(NavRoutes.DASHBOARD)
                        launchSingleTop = true
                    }
                },

                icon = {
                    Icon(item.icon, contentDescription = item.title)
                },

                label = {
                    Text(item.title)
                }
            )
        }
    }
}