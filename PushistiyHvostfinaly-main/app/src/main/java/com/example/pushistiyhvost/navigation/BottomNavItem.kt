package com.example.pushistiyhvost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Главная",
        route = Screen.Home.route,
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        title = "Каталог",
        route = Screen.Catalog.route,
        icon = Icons.Default.List
    ),
    BottomNavItem(
        title = "Корзина",
        route = Screen.Cart.route,
        icon = Icons.Default.ShoppingCart
    ),
    BottomNavItem(
        title = "Заказы",
        route = Screen.Orders.route,
        icon = Icons.Default.Inventory2
    ),
    BottomNavItem(
        title = "Профиль",
        route = Screen.Profile.route,
        icon = Icons.Default.Person
    )
)