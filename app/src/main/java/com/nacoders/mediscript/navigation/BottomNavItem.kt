package com.nacoders.mediscript.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(

    BottomNavItem(
        "Dashboard",
        NavRoutes.DASHBOARD,
        Icons.Default.Home
    ),

    BottomNavItem(
        "Patients",
        NavRoutes.PATIENT_LIST,
        Icons.Default.Person
    ),

    BottomNavItem(
        "Prescriptions",
        NavRoutes.PRESCRIPTION_LIST,
        Icons.Default.DateRange
    ),

    BottomNavItem(
        "Settings",
        NavRoutes.SETTINGS,
        Icons.Default.Settings
    )
)