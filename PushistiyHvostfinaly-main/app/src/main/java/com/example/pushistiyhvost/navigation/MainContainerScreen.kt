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
import com.example.pushistiyhvost.presentation.cart.CartScreen
import com.example.pushistiyhvost.presentation.catalog.CatalogProductsScreen
import com.example.pushistiyhvost.presentation.catalog.CatalogScreen
import com.example.pushistiyhvost.presentation.catalog.ProductDetailsScreen
import com.example.pushistiyhvost.presentation.checkout.CheckoutScreen
import com.example.pushistiyhvost.presentation.home.HomeScreen
import com.example.pushistiyhvost.presentation.orders.OrdersScreen
import com.example.pushistiyhvost.presentation.profile.ProfileScreen
import com.example.pushistiyhvost.ui.components.MainBottomBar
import com.example.pushistiyhvost.presentation.pets.AddEditPetScreen
import com.example.pushistiyhvost.presentation.pets.PetsScreen
import com.example.pushistiyhvost.presentation.bonus.BonusScreen
import com.example.pushistiyhvost.presentation.promotions.PromotionsScreen
import com.example.pushistiyhvost.presentation.articles.ArticleDetailsScreen
import com.example.pushistiyhvost.presentation.articles.ArticlesScreen

@Composable
fun MainContainerScreen(
    onLogout: () -> Unit,
    onLoginRequired: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            MainBottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onProductClick = { productId ->
                        bottomNavController.navigate("${Screen.ProductDetails.route}/$productId")
                    },
                    onArticleClick = { articleId ->
                        bottomNavController.navigate("${Screen.ArticleDetails.route}/$articleId")
                    },
                    onCatalogClick = {
                        bottomNavController.navigate(Screen.Catalog.route)
                    }
                )
            }

            composable(Screen.Catalog.route) {
                CatalogScreen(
                    onCategoryClick = { categoryTitle ->
                        bottomNavController.navigate("${Screen.CatalogProducts.route}/$categoryTitle")
                    }
                )
            }

            composable(
                route = "${Screen.CatalogProducts.route}/{categoryTitle}",
                arguments = listOf(
                    navArgument("categoryTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: "Каталог"
                CatalogProductsScreen(
                    categoryTitle = categoryTitle,
                    onProductClick = { productId ->
                        bottomNavController.navigate("${Screen.ProductDetails.route}/$productId")
                    }
                )
            }

            composable(
                route = "${Screen.ProductDetails.route}/{productId}",
                arguments = listOf(
                    navArgument("productId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailsScreen(productId = productId)
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    onCheckoutClick = {
                        bottomNavController.navigate(Screen.Checkout.route)
                    }
                )
            }

            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    onOrderConfirmed = {
                        bottomNavController.navigate(Screen.Orders.route) {
                            popUpTo(Screen.Checkout.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Orders.route) {
                OrdersScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogoutClick = onLogout,
                    onLoginRequiredClick = onLoginRequired,
                    onPetsClick = {
                        bottomNavController.navigate(Screen.Pets.route)
                    },
                    onBonusClick = {
                        bottomNavController.navigate(Screen.Bonus.route)
                    },
                    onPromotionsClick = {
                        bottomNavController.navigate(Screen.Promotions.route)
                    },
                    onArticlesClick = {
                        bottomNavController.navigate(Screen.Articles.route)
                    }
                )
            }

            composable(Screen.Pets.route) {
                PetsScreen(
                    onAddPetClick = {
                        bottomNavController.navigate(Screen.AddEditPet.route)
                    }
                )
            }

            composable(Screen.AddEditPet.route) {
                AddEditPetScreen(
                    onPetSaved = {
                        bottomNavController.popBackStack()
                    }
                )
            }

            composable(Screen.Bonus.route) {
                BonusScreen()
            }

            composable(Screen.Promotions.route) {
                PromotionsScreen()
            }

            composable(Screen.Articles.route) {
                ArticlesScreen(
                    onArticleClick = { articleId ->
                        bottomNavController.navigate("${Screen.ArticleDetails.route}/$articleId")
                    }
                )
            }

            composable(
                route = "${Screen.ArticleDetails.route}/{articleId}",
                arguments = listOf(
                    navArgument("articleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                ArticleDetailsScreen(articleId = articleId)
            }
        }
    }
}