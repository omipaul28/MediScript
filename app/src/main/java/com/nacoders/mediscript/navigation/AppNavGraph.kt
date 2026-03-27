package com.nacoders.mediscript.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.nacoders.mediscript.view.screens.AddPatientScreen
import com.nacoders.mediscript.view.screens.CreatePrescriptionScreen
import com.nacoders.mediscript.view.screens.DashboardScreen
import com.nacoders.mediscript.view.screens.LoginScreen
import com.nacoders.mediscript.view.screens.PatientDetailScreen
import com.nacoders.mediscript.view.screens.PatientListScreen
import com.nacoders.mediscript.view.screens.PrescriptionDetailScreen
import com.nacoders.mediscript.view.screens.PrescriptionHistoryScreen
import com.nacoders.mediscript.view.screens.RegisterScreen
import com.nacoders.mediscript.view.screens.SplashScreen
import com.nacoders.mediscript.viewmodel.AuthViewModel

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDest = if (currentUser != null) NavRoutes.DASHBOARD else NavRoutes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {

        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }

        composable(NavRoutes.LOGIN) {
            LoginScreen(navController, authViewModel)
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController, authViewModel)
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
            route = NavRoutes.CREATE_PRESCRIPTION + "?id={id}",
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