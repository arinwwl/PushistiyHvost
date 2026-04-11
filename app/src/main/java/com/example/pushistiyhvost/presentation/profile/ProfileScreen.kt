package com.example.pushistiyhvost.presentation.profile

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.data.repository.ProfileRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetOrdersUseCase
import com.example.pushistiyhvost.domain.usecase.GetProfileImageUseCase
import com.example.pushistiyhvost.domain.usecase.SaveProfileImageUseCase
import com.example.pushistiyhvost.presentation.orders.OrdersViewModel
import com.example.pushistiyhvost.presentation.orders.OrdersViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onLoginRequiredClick: () -> Unit,
    onPetsClick: () -> Unit,
    onBonusClick: () -> Unit,
    onPromotionsClick: () -> Unit,
    onArticlesClick: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isGuest = currentUser == null

    val firestore = FirebaseFirestore.getInstance()

    val orderRepository = OrderRepositoryImpl(firestore)
    val getOrdersUseCase = GetOrdersUseCase(orderRepository)

    val ordersViewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(getOrdersUseCase)
    )

    val profileRepository = ProfileRepositoryImpl(firestore)
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            getProfileImageUseCase = GetProfileImageUseCase(profileRepository),
            saveProfileImageUseCase = SaveProfileImageUseCase(profileRepository)
        )
    )

    val orders by ordersViewModel.orders.collectAsState()
    val profileImageBase64 by profileViewModel.profileImageBase64.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            ordersViewModel.loadOrders(userId)
            profileViewModel.loadProfileImage(userId)
        }
    }

    val displayName = currentUser?.email
        ?.substringBefore("@")
        ?.replaceFirstChar { it.uppercase() }
        ?: "Гость"

    val displayEmail = currentUser?.email ?: "Вход не выполнен"

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val userId = currentUser?.uid ?: return@rememberLauncherForActivityResult
        if (uri != null) {
            val base64 = uriToBase64(context, uri)
            if (base64 != null) {
                profileViewModel.saveProfileImage(userId, base64)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            if (!isGuest && profileImageBase64 != null) {
                val avatarBitmap = decodeBase64ToBitmap(profileImageBase64!!)

                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap.asImageBitmap(),
                        contentDescription = "Фото профиля",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .size(90.dp)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        shape = CircleShape,
                        color = Color(0xFFD9D9D9)
                    ) {}
                }
            } else {
                Surface(
                    modifier = Modifier
                        .size(90.dp)
                        .clickable(enabled = !isGuest) {
                            imagePickerLauncher.launch("image/*")
                        },
                    shape = CircleShape,
                    color = Color(0xFFD9D9D9)
                ) {}
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isGuest) {
                Text(
                    text = "Нажмите, чтобы выбрать фото",
                    color = Color(0xFF7A768C),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = displayName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = displayEmail,
                color = Color(0xFF7A768C),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (val state = profileUiState) {
                ProfileUiState.Idle -> Unit
                ProfileUiState.Loading -> CircularProgressIndicator()
                is ProfileUiState.Success -> {
                    Text(
                        text = state.message,
                        color = Color(0xFF2E7D32),
                        fontSize = 13.sp
                    )
                    LaunchedEffect(state.message) {
                        profileViewModel.resetState()
                    }
                }
                is ProfileUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Профиль",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text("Статус: ${if (isGuest) "Гостевой режим" else "Авторизован"}")

                    if (!isGuest) {
                        Text("Количество заказов: ${orders.size}")
                        Text(
                            text = "Бонусы",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onBonusClick() }
                        )
                        Text(
                            text = "Акции",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onPromotionsClick() }
                        )
                        Text(
                            text = "Статьи",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onArticlesClick() }
                        )
                        Text(
                            text = "Мои питомцы",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onPetsClick() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isGuest) {
                Button(
                    onClick = onLoginRequiredClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6C542),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Войти или зарегистрироваться",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6C542),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Выйти",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val originalBytes = inputStream.readBytes()
        inputStream.close()

        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size) ?: return null

        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 40, outputStream)

        val compressedBytes = outputStream.toByteArray()
        Base64.encodeToString(compressedBytes, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }
}

private fun decodeBase64ToBitmap(base64: String) =
    try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }