package com.example.myspec.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myspec.components.controls.BottomNavBar
import com.example.myspec.components.ErrorLogo
import com.example.myspec.components.speciality.SpecialityCard
import com.example.myspec.components.speciality.SpecialityDialog
import com.example.myspec.models.Program
import com.example.myspec.ui.theme.Orange
import com.example.myspec.viewmodels.main.ListViewModel


@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = viewModel(),
    isFavoriteList: Boolean = false
) {
    val specialties by viewModel.specialties.collectAsState()
    val loading by if (isFavoriteList) viewModel.hasMore.collectAsState() else
        viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedProgram by remember { mutableStateOf<Program?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredSpecialties = specialties.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true) ||
                it.university.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        viewModel.loadInitialData(isFavoriteList)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    )
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = if (isFavoriteList) "Избранное" else "Рекомендации",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                color = Orange,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Поиск...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                shape = RoundedCornerShape(16.dp),
            )

            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Orange)
                    }
                }

                error != null -> {
                    ErrorLogo { viewModel.loadInitialData(isFavoriteList) }
                }

                else -> {
                    if (filteredSpecialties.isEmpty()) {
                        ErrorLogo (
                            text = "К сожалению по вашим критериям не найдено ничего подходящего!",
                            reTryButton = false
                        ) { }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredSpecialties) { program ->
                            SpecialityCard(
                                program = program,
                                onClick = { selectedProgram = program },
                                onFavoriteClick = { viewModel.toggleFavorite(program.id) }
                            )
                        }
                    }
                }
            }
        }

        selectedProgram?.let { program ->
            SpecialityDialog(
                program = program,
                onDismissRequest = { selectedProgram = null },
                onFavoriteClick = { viewModel.toggleFavorite(program.id) }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavBar(navController = navController)
        }
    }
}