package com.example.pushistiyhvost.presentation.pets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.PetRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.AddPetUseCase
import com.example.pushistiyhvost.domain.usecase.GetPetsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.width

@Composable
fun PetsScreen(
    onAddPetClick: () -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val firestore = FirebaseFirestore.getInstance()
    val repository = PetRepositoryImpl(firestore)
    val getPetsUseCase = GetPetsUseCase(repository)
    val addPetUseCase = AddPetUseCase(repository)

    val viewModel: PetsViewModel = viewModel(
        factory = PetsViewModelFactory(getPetsUseCase, addPetUseCase)
    )

    val pets by viewModel.pets.collectAsState()

    LaunchedEffect(userId) {
        if (userId != "guest") {
            viewModel.loadPets(userId)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Мои питомцы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (userId == "guest") {
                Text("В гостевом режиме питомцы недоступны")
            } else {
                Button(
                    onClick = onAddPetClick,
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
                        text = "Добавить питомца",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (pets.isEmpty()) {
                    Text("У вас пока нет добавленных питомцев")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pets) { pet ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(52.dp),
                                        shape = CircleShape,
                                        color = Color(0xFFE7E2F3)
                                    ) {}

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = pet.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            text = "${pet.type} • ${pet.breed}",
                                            color = Color(0xFF7A768C)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}