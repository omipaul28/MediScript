package com.nacoders.mediscript.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.nacoders.mediscript.view.screens.AddPatientScreen
import com.nacoders.mediscript.view.screens.CreatePrescriptionScreen
import com.nacoders.mediscript.view.screens.DashboardScreen
import com.nacoders.mediscript.view.screens.LoginScreen
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

        composable(NavRoutes.CREATE_PRESCRIPTION) {
            CreatePrescriptionScreen(navController)
        }

        composable(NavRoutes.PRESCRIPTION_HISTORY) {
            PrescriptionHistoryScreen(navController)
        }

        composable(
            route = NavRoutes.PRESCRIPTION_DETAIL + "/{id}"
        ) {

            val id = it.arguments?.getString("id")

            PrescriptionDetailScreen(
                navController,
                prescriptionId = id ?: ""
            )
        }
//
//        composable(NavRoutes.SETTINGS) {
//            SettingsScreen(navController)
//        }
    }
}