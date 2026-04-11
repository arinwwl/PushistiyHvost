package com.example.pushistiyhvost.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pushistiyhvost.presentation.admin.AdminArticleFormScreen
import com.example.pushistiyhvost.presentation.admin.AdminArticlesScreen
import com.example.pushistiyhvost.presentation.admin.AdminHomeScreen
import com.example.pushistiyhvost.presentation.admin.AdminNotificationsScreen
import com.example.pushistiyhvost.presentation.admin.AdminOrderDetailsScreen
import com.example.pushistiyhvost.presentation.admin.AdminOrdersScreen
import com.example.pushistiyhvost.presentation.admin.AdminProductFormScreen
import com.example.pushistiyhvost.presentation.admin.AdminProductsScreen
import com.example.pushistiyhvost.presentation.admin.AdminProfileScreen
import com.example.pushistiyhvost.presentation.admin.AdminPromotionFormScreen
import com.example.pushistiyhvost.presentation.admin.AdminPromotionsScreen
import com.example.pushistiyhvost.presentation.admin.AdminStatisticsScreen
import com.example.pushistiyhvost.ui.components.AdminBottomBar

@Composable
fun AdminContainerScreen(
    onLogout: () -> Unit
) {
    val adminNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            AdminBottomBar(navController = adminNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = adminNavController,
            startDestination = Screen.AdminHome.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AdminHome.route) {
                AdminHomeScreen(
                    onAddProductClick = {
                        adminNavController.navigate(Screen.AdminProductForm.route)
                    },
                    onCreatePromotionClick = {
                        adminNavController.navigate(Screen.AdminPromotionForm.route)
                    },
                    onAddArticleClick = {
                        adminNavController.navigate(Screen.AdminArticleForm.route)
                    },
                    onOrdersClick = {
                        adminNavController.navigate(Screen.AdminOrders.route)
                    },
                    onPromotionsClick = {
                        adminNavController.navigate(Screen.AdminPromotions.route)
                    },
                    onArticlesClick = {
                        adminNavController.navigate(Screen.AdminArticles.route)
                    },
                    onNotificationsClick = {
                        adminNavController.navigate(Screen.AdminNotifications.route)
                    }
                )
            }

            composable(Screen.AdminProducts.route) {
                AdminProductsScreen(
                    onAddProductClick = {
                        adminNavController.navigate(Screen.AdminProductForm.route)
                    },
                    onEditProductClick = { productId ->
                        adminNavController.navigate("${Screen.AdminProductForm.route}/$productId")
                    }
                )
            }

            composable(Screen.AdminOrders.route) {
                AdminOrdersScreen(
                    onOrderClick = { orderId ->
                        println("OPEN ORDER DETAILS: $orderId")
                        adminNavController.navigate("${Screen.AdminOrderDetails.route}/$orderId")
                    }
                )
            }

            composable(
                route = "${Screen.AdminOrderDetails.route}/{orderId}",
                arguments = listOf(
                    navArgument("orderId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""

                AdminOrderDetailsScreen(
                    orderId = orderId,
                    onBack = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(Screen.AdminStatistics.route) {
                AdminStatisticsScreen()
            }

            composable(Screen.AdminProfile.route) {
                AdminProfileScreen(
                    onLogoutClick = onLogout
                )
            }

            composable(Screen.AdminPromotions.route) {
                AdminPromotionsScreen(
                    onCreatePromotionClick = {
                        adminNavController.navigate(Screen.AdminPromotionForm.route)
                    },
                    onEditPromotionClick = { promotionId ->
                        adminNavController.navigate("${Screen.AdminPromotionForm.route}/$promotionId")
                    }
                )
            }

            composable(Screen.AdminArticles.route) {
                AdminArticlesScreen(
                    onAddArticleClick = {
                        adminNavController.navigate(Screen.AdminArticleForm.route)
                    },
                    onEditArticleClick = { articleId ->
                        adminNavController.navigate("${Screen.AdminArticleForm.route}/$articleId")
                    }
                )
            }

            composable(Screen.AdminNotifications.route) {
                AdminNotificationsScreen()
            }

            composable(Screen.AdminProductForm.route) {
                AdminProductFormScreen(
                    productId = null,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(
                route = "${Screen.AdminProductForm.route}/{productId}",
                arguments = listOf(
                    navArgument("productId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                AdminProductFormScreen(
                    productId = productId,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(Screen.AdminPromotionForm.route) {
                AdminPromotionFormScreen(
                    promotionId = null,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(
                route = "${Screen.AdminPromotionForm.route}/{promotionId}",
                arguments = listOf(
                    navArgument("promotionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val promotionId = backStackEntry.arguments?.getString("promotionId")
                AdminPromotionFormScreen(
                    promotionId = promotionId,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(Screen.AdminArticleForm.route) {
                AdminArticleFormScreen(
                    articleId = null,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }

            composable(
                route = "${Screen.AdminArticleForm.route}/{articleId}",
                arguments = listOf(
                    navArgument("articleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId")
                AdminArticleFormScreen(
                    articleId = articleId,
                    onSaved = {
                        adminNavController.popBackStack()
                    }
                )
            }
        }
    }
}