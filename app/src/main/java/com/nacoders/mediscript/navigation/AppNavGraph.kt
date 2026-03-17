package com.nacoders.mediscript.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.nacoders.mediscript.view.screens.AddPatientScreen
import com.nacoders.mediscript.view.screens.CreatePrescriptionScreen
import com.nacoders.mediscript.view.screens.DashboardScreen
import com.nacoders.mediscript.view.screens.LoginScreen
import com.nacoders.mediscript.view.screens.PatientDetailScreen
import com.nacoders.mediscript.view.screens.PatientListScreen
import com.nacoders.mediscript.view.screens.PrescriptionDetailScreen
import com.nacoders.mediscript.view.screens.PrescriptionHistoryScreen
import com.nacoders.mediscript.view.screens.RegisterScreen
import com.nacoders.mediscript.view.screens.SettingsScreen
import com.nacoders.mediscript.view.screens.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.DASHBOARD
    ) {

        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }

        composable(NavRoutes.LOGIN) {
            LoginScreen(navController)
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController)
        }

        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(NavRoutes.PATIENT_LIST) {
            PatientListScreen(navController)
        }

        composable(NavRoutes.ADD_PATIENT) {
            AddPatientScreen(navController)
        }

        composable(
            route = NavRoutes.CREATE_PRESCRIPTION + "?id={id}", // Use query param syntax for optional
            arguments = listOf(navArgument("id") {
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            CreatePrescriptionScreen(navController, patientId = id)
        }

        composable(NavRoutes.PRESCRIPTION_LIST) {
            PrescriptionHistoryScreen(navController)
        }

        composable(
            route = NavRoutes.PRESCRIPTION_DETAIL + "/{id}"
        ) {

            val id = it.arguments?.getString("id")

            PrescriptionDetailScreen(
                navController,
                patientId = id ?: ""
            )
        }
        composable(
            route = NavRoutes.PATIENT_DETAIL + "/{id}"
        ) {

            val id = it.arguments?.getString("id")

            PatientDetailScreen(
                navController,
                patientId = id ?: ""
            )
        }
//
//        composable(NavRoutes.SETTINGS) {
//            SettingsScreen(navController)
//        }
    }
}