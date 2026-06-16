package com.example.pushistiyhvost.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pushistiyhvost.R
import com.example.pushistiyhvost.ui.components.PrimaryAuthButton
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPersonalDataConsentChecked by remember { mutableStateOf(false) }
    var showPersonalDataConsentDialog by remember { mutableStateOf(false) }

    val state by viewModel.authState.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            AuthUiState.SuccessUser -> {
                onSuccess()
                viewModel.resetState()
            }
            AuthUiState.SuccessAdmin -> {
                viewModel.resetState()
            }
            else -> Unit
        }
    }
    if (showPersonalDataConsentDialog) {
        AlertDialog(
            onDismissRequest = {
                showPersonalDataConsentDialog = false
            },
            title = {
                Text(
                    text = "Согласие на обработку персональных данных",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = personalDataConsentText,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPersonalDataConsentDialog = false
                    }
                ) {
                    Text("Понятно")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(28.dp))
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(
                                color = Color(0xFFF5F3FA),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Назад"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = painterResource(id = R.drawable.register_top),
                    contentDescription = "Верхнее изображение регистрации",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Регистрация",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Телефон или email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.Visibility
                                } else {
                                    Icons.Default.VisibilityOff
                                },
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPersonalDataConsentChecked,
                        onCheckedChange = {
                            isPersonalDataConsentChecked = it
                        }
                    )

                    Text(
                        text = "Я даю согласие на обработку персональных данных",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable {
                            showPersonalDataConsentDialog = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryAuthButton(
                    text = "Зарегистрироваться",
                    enabled = isPersonalDataConsentChecked,
                    onClick = {
                        viewModel.register(email, password)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Уже есть аккаунт? ",
                        color = Color(0xFF8C889B)
                    )
                    Text(
                        text = "Войти",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val currentState = state) {
                    AuthUiState.Idle -> Unit

                    AuthUiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    AuthUiState.SuccessUser -> {
                        Text("Успешная регистрация")
                    }

                    AuthUiState.SuccessAdmin -> {
                        Text("Успешный вход")
                    }

                    is AuthUiState.Error -> {
                        Text(
                            text = currentState.message,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }

}
private val personalDataConsentText = """
    Настоящим я, пользователь мобильного приложения «Пушистый Хвост», свободно, своей волей и в своем интересе даю согласие на обработку моих персональных данных.

    Оператор персональных данных: зоомагазин «Пушистый Хвост».

    Цель обработки персональных данных: регистрация пользователя в мобильном приложении, создание и обслуживание учетной записи, оформление и обработка заказов, обратная связь с пользователем, предоставление информации о товарах, заказах, акциях и бонусной программе.

    Перечень обрабатываемых персональных данных:
    — адрес электронной почты;
    — номер телефона, если он указывается пользователем;
    — имя пользователя, если оно указывается пользователем;
    — адрес доставки, если он указывается при оформлении заказа;
    — сведения о заказах;
    — данные о питомцах, если пользователь добавляет их в приложении.

    Я разрешаю Оператору совершать с моими персональными данными следующие действия: сбор, запись, систематизацию, накопление, хранение, уточнение, обновление, изменение, использование, передачу в случаях, необходимых для выполнения заказа, обезличивание, блокирование, удаление и уничтожение.

    Обработка персональных данных может осуществляться с использованием средств автоматизации.

    Согласие действует с момента его предоставления и до момента отзыва пользователем. Пользователь вправе отозвать согласие на обработку персональных данных путем направления соответствующего обращения Оператору.

    Подтверждая согласие, я подтверждаю, что ознакомлен(а) с условиями обработки персональных данных и согласен(на) на их обработку.
""".trimIndent()