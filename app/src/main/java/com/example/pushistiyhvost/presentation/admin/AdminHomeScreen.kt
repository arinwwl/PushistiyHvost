package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHomeScreen(
    onAddProductClick: () -> Unit,
    onCreatePromotionClick: () -> Unit,
    onAddArticleClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onPromotionsClick: () -> Unit,
    onArticlesClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Главная",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Статистика")
                Text("Заказы: 24")
                Text("Выручка: 18 450 ₽")
            }
        }

        Button(
            onClick = onAddProductClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить товар")
        }

        Button(
            onClick = onCreatePromotionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать акцию")
        }

        Button(
            onClick = onAddArticleClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить статью")
        }

        Button(
            onClick = onOrdersClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Последние заказы")
        }

        Button(
            onClick = onPromotionsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Акции")
        }

        Button(
            onClick = onArticlesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Статьи")
        }

        Button(
            onClick = onNotificationsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Уведомления")
        }
    }
}