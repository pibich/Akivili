package com.akivili.app.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.akivili.app.core.navigation.Screen
import com.akivili.app.presentation.auth.ForgotPasswordScreen
import com.akivili.app.presentation.auth.LoginScreen
import com.akivili.app.presentation.auth.RegisterScreen
import com.akivili.app.presentation.components.AkiviliBottomBar
import com.akivili.app.presentation.help.HelpCenterScreen
import com.akivili.app.presentation.history.HistoryScreen
import com.akivili.app.presentation.home.HomeScreen
import com.akivili.app.presentation.payment.PaymentScreen
import com.akivili.app.presentation.payment.QRISScreen
import com.akivili.app.presentation.profile.ProfileScreen
import com.akivili.app.presentation.splash.SplashScreen
import com.akivili.app.presentation.topup.TopupScreen

@Composable
fun AkiviliApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.History.route,
        Screen.HelpCenter.route,
        Screen.Profile.route
    )
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AkiviliBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // Auth Screens
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Screen.ForgotPassword.route)
                    },
                    onContinueAsGuest = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onBackClick = { navController.popBackStack() },
                    onSubmitSuccess = { navController.popBackStack() }
                )
            }

            // Bottom Navigation Destinations
            composable(Screen.Home.route) {
                HomeScreen(
                    onGameSelected = { gameId ->
                        navController.navigate(Screen.Topup.createRoute(gameId))
                    }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    onTransactionClick = { invoiceId ->
                        navController.navigate(Screen.QRISInvoice.createRoute(invoiceId))
                    }
                )
            }

            composable(Screen.HelpCenter.route) {
                HelpCenterScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // Topup Flow
            composable(
                route = Screen.Topup.route,
                arguments = listOf(navArgument("gameId") { type = NavType.StringType })
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId") ?: "mlbb"
                TopupScreen(
                    gameId = gameId,
                    onBackClick = { navController.popBackStack() },
                    onProceedToPayment = { gId, pId, uId, sId ->
                        navController.navigate(Screen.Payment.createRoute(gId, pId, uId, sId))
                    }
                )
            }

            // Payment Method Selection
            composable(
                route = Screen.Payment.route,
                arguments = listOf(
                    navArgument("gameId") { type = NavType.StringType },
                    navArgument("packageId") { type = NavType.StringType },
                    navArgument("userId") { type = NavType.StringType },
                    navArgument("serverId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId") ?: "mlbb"
                val packageId = backStackEntry.arguments?.getString("packageId") ?: ""
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                val serverId = backStackEntry.arguments?.getString("serverId")

                PaymentScreen(
                    gameId = gameId,
                    packageId = packageId,
                    userId = userId,
                    serverId = serverId,
                    onBackClick = { navController.popBackStack() },
                    onPaymentSuccessProceed = { invoiceId ->
                        navController.navigate(Screen.QRISInvoice.createRoute(invoiceId))
                    }
                )
            }

            // QRIS & Invoice Screen
            composable(
                route = Screen.QRISInvoice.route,
                arguments = listOf(navArgument("invoiceId") { type = NavType.StringType })
            ) { backStackEntry ->
                val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: "INV-AKV-001"
                QRISScreen(
                    invoiceId = invoiceId,
                    onBackToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
