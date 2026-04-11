package com.example.pushistiyhvost.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")

    data object Main : Screen("main")
    data object AdminMain : Screen("admin_main")

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
    data object Bonus : Screen("bonus")

    data object Promotions : Screen("promotions")
    data object Articles : Screen("articles")
    data object ArticleDetails : Screen("article_details")


    data object AdminHome : Screen("admin_home")
    data object AdminProducts : Screen("admin_products")
    data object AdminProductForm : Screen("admin_product_form")
    data object AdminOrders : Screen("admin_orders")
    data object AdminOrderDetails : Screen("admin_order_details")
    data object AdminStatistics : Screen("admin_statistics")
    data object AdminProfile : Screen("admin_profile")
    data object AdminPromotions : Screen("admin_promotions")
    data object AdminPromotionForm : Screen("admin_promotion_form")
    data object AdminArticles : Screen("admin_articles")
    data object AdminArticleForm : Screen("admin_article_form")
    data object AdminNotifications : Screen("admin_notifications")

}