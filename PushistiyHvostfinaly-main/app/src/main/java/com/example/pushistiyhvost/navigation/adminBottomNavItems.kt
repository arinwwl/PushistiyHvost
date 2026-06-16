package com.example.pushistiyhvost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueryStats

val adminBottomNavItems = listOf(
    AdminBottomNavItem(
        title = "Главная",
        icon = Icons.Default.Home,
        route = Screen.AdminHome.route
    ),
    AdminBottomNavItem(
        title = "Товары",
        icon = Icons.Default.Inventory2,
        route = Screen.AdminProducts.route
    ),
    AdminBottomNavItem(
        title = "Заказы",
        icon = Icons.Default.ListAlt,
        route = Screen.AdminOrders.route
    ),
    AdminBottomNavItem(
        title = "Статистика",
        icon = Icons.Default.QueryStats,
        route = Screen.AdminStatistics.route
    ),
    AdminBottomNavItem(
        title = "Профиль",
        icon = Icons.Default.Person,
        route = Screen.AdminProfile.route
    )
)