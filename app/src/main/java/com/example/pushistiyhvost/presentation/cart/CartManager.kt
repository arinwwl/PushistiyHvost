package com.example.pushistiyhvost.presentation.cart

import androidx.compose.runtime.mutableStateListOf
import com.example.pushistiyhvost.presentation.cart.model.CartItem
import com.example.pushistiyhvost.presentation.home.model.Product

object CartManager {

    val cartItems = mutableStateListOf<CartItem>()

    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }

        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            cartItems.add(CartItem(product = product, quantity = 1))
        }
    }

    fun increaseQuantity(productId: String) {
        val existingItem = cartItems.find { it.product.id == productId } ?: return
        val index = cartItems.indexOf(existingItem)
        cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
    }

    fun decreaseQuantity(productId: String) {
        val existingItem = cartItems.find { it.product.id == productId } ?: return
        val index = cartItems.indexOf(existingItem)

        if (existingItem.quantity > 1) {
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity - 1)
        } else {
            cartItems.remove(existingItem)
        }
    }

    fun getTotalPrice(): Int {
        return cartItems.sumOf { it.product.price * it.quantity }
    }
    fun clearCart() {
        cartItems.clear()
    }
}