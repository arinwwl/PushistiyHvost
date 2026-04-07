package com.example.pushistiyhvost.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")

    data object Main : Screen("main")

    data object Home : Screen("home")
    data object Catalog : Screen("catalog")
    data object CatalogProducts : Screen("catalog_products")
    data object ProductDetails : Screen("product_details")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object Orders : Screen("orders")
    data object Profile : Screen("profile")

    data object Pets : Screen("pets")
    data object AddEditPet : Screen("add_edit_pet")
}