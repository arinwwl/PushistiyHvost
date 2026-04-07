package com.example.pushistiyhvost.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pushistiyhvost.data.repository.AuthRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.LoginUseCase
import com.example.pushistiyhvost.domain.usecase.RegisterUseCase
import com.example.pushistiyhvost.presentation.auth.AuthViewModel
import com.example.pushistiyhvost.presentation.auth.AuthViewModelFactory
import com.example.pushistiyhvost.presentation.auth.LoginScreen
import com.example.pushistiyhvost.presentation.auth.RegisterScreen
import com.example.pushistiyhvost.presentation.auth.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val firebaseAuth = FirebaseAuth.getInstance()
    val repository = AuthRepositoryImpl(firebaseAuth)
    val registerUseCase = RegisterUseCase(repository)
    val loginUseCase = LoginUseCase(repository)

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(registerUseCase, loginUseCase)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onGuestClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Main.route) {
            MainContainerScreen(
                onLogout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onLoginRequired = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}