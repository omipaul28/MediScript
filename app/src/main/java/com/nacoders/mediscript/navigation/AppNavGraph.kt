package com.nacoders.mediscript.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.google.firebase.auth.FirebaseAuth
import com.nacoders.mediscript.view.screens.*
import com.nacoders.mediscript.viewmodel.AuthViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Check initial auth state
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDest = if (currentUser != null) NavRoutes.DASHBOARD else NavRoutes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDest,
        // Global transitions for a premium feel
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) }
    ) {
        // --- Auth Screens ---
        composable(NavRoutes.LOGIN) {
            LoginScreen(navController, authViewModel)
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(navController, authViewModel)
        }

        // --- Core App ---
        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(navController)
        }

        // --- Patient Flow ---
        composable(NavRoutes.PATIENT_LIST) {
            PatientListScreen(navController)
        }

        composable(NavRoutes.ADD_PATIENT) {
            AddPatientScreen(navController)
        }

        composable(
            route = "${NavRoutes.PATIENT_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PatientDetailScreen(navController, patientId = id)
        }

        // --- Prescription Flow ---
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
            route = "${NavRoutes.PRESCRIPTION_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PrescriptionDetailScreen(navController, patientId = id)
        }
    }
}