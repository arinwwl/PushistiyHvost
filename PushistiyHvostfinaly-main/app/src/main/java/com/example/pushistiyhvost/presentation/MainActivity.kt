package com.example.pushistiyhvost.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pushistiyhvost.navigation.AppNavigation
import com.example.pushistiyhvost.ui.theme.PushistiyHvostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PushistiyHvostTheme {
                AppNavigation()
            }
        }
    }
}