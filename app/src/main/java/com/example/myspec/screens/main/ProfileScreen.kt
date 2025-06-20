package com.example.myspec.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myspec.R
import com.example.myspec.components.controls.AchievementCheckbox
import com.example.myspec.components.controls.BottomNavBar
import com.example.myspec.components.ErrorLogo
import com.example.myspec.components.LoadingAnimation
import com.example.myspec.components.controls.buttons.GradientButton
import com.example.myspec.components.controls.TriStateCheckbox
import com.example.myspec.components.controls.fields.IntegerInputField
import com.example.myspec.components.text.ThinText
import com.example.myspec.constants.Other
import com.example.myspec.models.Subject
import com.example.myspec.models.toCodeNameStrings
import com.example.myspec.ui.theme.Orange
import com.example.myspec.ui.theme.White
import com.example.myspec.viewmodels.main.ProfileViewModel


@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {


    val state by viewModel.state.collectAsState()
    val email by viewModel.email.collectAsState()
    val specialties by viewModel.specialties.collectAsState()
    val selectedSpecialties by viewModel.selectedSpecialties.collectAsState()
    val subjects by viewModel.selectedSubjects.collectAsState()
    val bonusList by viewModel.bonusList.collectAsState()

    var showEgeDialog by remember { mutableStateOf(false) }
    var showSubjectsDialog by remember { mutableStateOf(false) }
    var showSpecialtiesDialog by remember { mutableStateOf(false) }

    val exit by viewModel.exit.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val expandedGroups = viewModel.expandedGroups
    val expandedDirections = viewModel.expandedDirections
    var expandedSection by remember { mutableStateOf<String?>("selfData") }


    fun toggleAccordion(block: String) {
        expandedSection = if (expandedSection == block) null else block
    }


    LaunchedEffect(Unit) {
        viewModel.loadingProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            exit -> {
                navController.navigate("auth") {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                }
            }

            loading -> {
                LoadingAnimation()
            }


            error != null -> {
                ErrorLogo { viewModel.loadingProfile() }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.87f)
                            .clipToBounds()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(48.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(40.dp)
                            )

                            Text(
                                text = "Мой профиль",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Orange,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1.5f, fill = false)
                            )

                            IconButton(
                                onClick = { viewModel.logout() },
                                modifier = Modifier
                                    .size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout),
                                    contentDescription = null,
                                    tint = Orange,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            onClick = { toggleAccordion("selfData") },
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Личные данные",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    IconButton(
                                        onClick = { toggleAccordion("selfData") },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (expandedSection == "selfData") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (expandedSection == "selfData") "Свернуть" else "Развернуть"
                                        )
                                    }
                                }

                                if (expandedSection == "selfData") {
                                    OutlinedTextField(
                                        enabled = false,
                                        value = email,
                                        onValueChange = { },
                                        label = { Text("Email") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                    )

                                    OutlinedTextField(
                                        enabled = false,
                                        value = "password",
                                        onValueChange = {},
                                        label = { Text("Пароль") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        visualTransformation = PasswordVisualTransformation(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                        trailingIcon = {
                                            Row(modifier = Modifier.width(150.dp)) {
                                                GradientButton(
                                                    stringId = R.string.change,
                                                    paddingBottom = 15,
                                                    paddingTop = 15
                                                ) { viewModel.resetPassword()}
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        if (state != 4) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                onClick = {
                                    toggleAccordion("subject")
                                },
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shadowElevation = 2.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Предметы ЕГЭ",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        IconButton(
                                            onClick = {
                                                toggleAccordion("subject")
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (expandedSection == "subject") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                contentDescription = if (expandedSection == "subject") "Свернуть" else "Развернуть"
                                            )
                                        }
                                    }

                                    if (expandedSection == "subject") {
                                        var text = R.string.change
                                        if (state == 1) {
                                            text = R.string.choise
                                        } else {
                                            if (subjects.isNotEmpty()) {
                                                ProfileInfoItem(subjects, state)
                                            }
                                            GradientButton(
                                                stringId = R.string.ballInput,
                                                paddingTop = 0,
                                                paddingBottom = 0
                                            ) { showEgeDialog = true }
                                        }
                                        GradientButton(
                                            stringId = text,
                                            paddingTop = 0,
                                            paddingBottom = 0
                                        ) {
                                            showSubjectsDialog = true
                                        }
                                    }
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            onClick = { toggleAccordion("bonus") },
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Достижения",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    IconButton(
                                        onClick = {
                                            toggleAccordion("bonus")
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (expandedSection == "bonus") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (expandedSection == "bonus") "Свернуть" else "Развернуть"
                                        )
                                    }
                                }

                                if (expandedSection == "bonus") {
                                    Text(
                                        text = "Отметьте ваши достижения",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    AchievementCheckbox(
                                        text = "Золотой значок ГТО",
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_sports),
                                        checked = bonusList.isGTO,
                                        onClick = { viewModel.toggleBonus(1) },
                                    )

                                    if (state == 4) {
                                        AchievementCheckbox(
                                            text = "Диплом СПО с отличием",
                                            icon = ImageVector.vectorResource(id = R.drawable.ic_workspace),
                                            checked = bonusList.isPerfectSPO,
                                            onClick = { viewModel.toggleBonus(3) },
                                        )
                                    } else {
                                        AchievementCheckbox(
                                            text = "Аттестат с отличием",
                                            icon = ImageVector.vectorResource(id = R.drawable.ic_workspace),
                                            checked = bonusList.isPefectAttestat,
                                            onClick = { viewModel.toggleBonus(2) },
                                        )
                                    }


                                    AchievementCheckbox(
                                        text = "Портфолио/олимпиады",
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_event),
                                        checked = bonusList.isPortfolio,
                                        onClick = { viewModel.toggleBonus(4) },
                                    )

                                    AchievementCheckbox(
                                        text = "Волонтерство",
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_volunteer),
                                        checked = bonusList.isVolunteer,
                                        onClick = { viewModel.toggleBonus(5) },
                                    )

                                    AchievementCheckbox(
                                        text = "Итоговое сочинение",
                                        icon = ImageVector.vectorResource(id = R.drawable.ic_draw),
                                        checked = bonusList.isEssay,
                                        onClick = { viewModel.toggleBonus(6) },
                                    )

                                    GradientButton(
                                        stringId = R.string.save,
                                    )
                                    { viewModel.saveBonus() }
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)),
                            onClick = {
                                toggleAccordion("speciality")
                            },
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Специальности",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    IconButton(
                                        onClick = {
                                            toggleAccordion("speciality")
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (expandedSection == "speciality") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (expandedSection == "speciality") "Свернуть" else "Развернуть"
                                        )
                                    }
                                }

                                if (expandedSection == "speciality") {
                                    ProfileSpecialityItem(selectedSpecialties)
                                    GradientButton(
                                        stringId = R.string.profile_check_speciality,
                                        paddingTop = 0,
                                    )
                                    {
                                        showSpecialtiesDialog = true
                                    }
                                }
                            }
                        }

                    }
                }

                if (showSubjectsDialog) {
                    AlertDialog(
                        onDismissRequest = { showSubjectsDialog = false },
                        title = { Text("Выберите предметы") },
                        text = {
                            val scrollState = rememberScrollState()
                            Box(
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                                    .clipToBounds()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 0.dp,
                                                bottomEnd = 0.dp
                                            )
                                        )
                                ) {
                                    Other.egeSubjects.forEach { subject ->
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.toggleSubject(Subject(subject))
                                                }
                                                .padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = subjects.any { it.subject == subject },
                                                onCheckedChange = { isChecked ->
                                                    viewModel.toggleSubject(
                                                        Subject(subject),
                                                        isChecked
                                                    )
                                                }
                                            )
                                            Text(subject, modifier = Modifier.padding(start = 8.dp))
                                        }
                                    }

                                }

                            }
                        },
                        dismissButton = {
                            GradientButton(
                                stringId = R.string.button_cancel,
                                paddingBottom = 0,
                                paddingTop = 5
                            ) {
                                showSubjectsDialog = false;
                            }
                        },
                        confirmButton = {
                            GradientButton(
                                stringId = R.string.button_succes,
                                paddingBottom = 0,
                                paddingTop = 5
                            ) {
                                showSubjectsDialog = false;
                                viewModel.saveSubjectsUpdate()
                            }
                        }
                    )
                }

                if (showEgeDialog) {
                    AlertDialog(
                        onDismissRequest = { showEgeDialog = false },
                        title = { Text("Введите результаты ЕГЭ") },
                        text = {
                            val scrollState = rememberScrollState()
                            Box(
                                modifier = Modifier
                                    .heightIn(max = 400.dp)
                                    .clipToBounds()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 0.dp,
                                                bottomEnd = 0.dp
                                            )
                                        )
                                ) {
                                    subjects.forEach { subject ->
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                subject.subject,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                            IntegerInputField(
                                                value = subject.ball,
                                                onValueChange = { number -> subject.ball = number })
                                        }
                                    }

                                }

                            }
                        },
                        dismissButton = {
                            GradientButton(
                                stringId = R.string.button_cancel,
                                paddingBottom = 0,
                                paddingTop = 5
                            ) {
                                showEgeDialog = false;
                            }
                        },
                        confirmButton = {
                            GradientButton(
                                stringId = R.string.button_succes,
                                paddingBottom = 0,
                                paddingTop = 5
                            ) {
                                showEgeDialog = false;
                                viewModel.saveSubjectsUpdate()
                            }
                        }
                    )
                }

                if (showSpecialtiesDialog) {
                    AlertDialog(
                        onDismissRequest = { showSpecialtiesDialog = false },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ThinText(R.string.select_specialties)
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .heightIn(max = 300.dp)
                                        .verticalScroll(rememberScrollState())
                                        .fillMaxWidth()
                                ) {
                                    Column {
                                        specialties?.forEach { (group, directionsMap) ->
                                            val groupSpecialties =
                                                directionsMap.flatMap { it.value }
                                            val isGroupExpanded = group in expandedGroups
                                            val groupSelectionState = when {
                                                selectedSpecialties.containsAll(groupSpecialties.toCodeNameStrings()) -> true
                                                selectedSpecialties.none { it in groupSpecialties.toCodeNameStrings() } -> false
                                                else -> null
                                            }
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable {
                                                            viewModel.toggleGroupExpansion(group)
                                                        }
                                                ) {
                                                    TriStateCheckbox(
                                                        state = groupSelectionState,
                                                        onClick = {
                                                            viewModel.toggleGroupSelection(group)
                                                        }
                                                    )
                                                    Icon(
                                                        imageVector = if (isGroupExpanded)
                                                            Icons.Default.KeyboardArrowUp
                                                        else
                                                            Icons.Default.KeyboardArrowDown,
                                                        contentDescription = null,
                                                        tint = White
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(text = group, color = White)
                                                }

                                                if (isGroupExpanded) {
                                                    directionsMap.forEach { (direction, specialtiesList) ->
                                                        val isDirectionExpanded =
                                                            direction in expandedDirections
                                                        val directionSelectionState = when {
                                                            selectedSpecialties.containsAll(
                                                                specialtiesList.toCodeNameStrings()
                                                            ) -> true

                                                            selectedSpecialties.none { it in specialtiesList.toCodeNameStrings() } -> false
                                                            else -> null
                                                        }

                                                        Column(modifier = Modifier.padding(start = 32.dp)) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(
                                                                        vertical = 8.dp
                                                                    )
                                                                    .clickable {
                                                                        viewModel.toggleDirectionExpansion(
                                                                            direction
                                                                        )
                                                                    }
                                                            ) {
                                                                TriStateCheckbox(
                                                                    state = directionSelectionState,
                                                                    onClick = {
                                                                        viewModel.toggleDirectionSelection(
                                                                            group,
                                                                            direction
                                                                        )
                                                                    }
                                                                )
                                                                Icon(
                                                                    imageVector = if (isDirectionExpanded)
                                                                        Icons.Default.KeyboardArrowUp
                                                                    else
                                                                        Icons.Default.KeyboardArrowDown,
                                                                    contentDescription = null,
                                                                    tint = White
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Text(
                                                                    text = direction,
                                                                    color = White
                                                                )
                                                            }

                                                            if (isDirectionExpanded) {
                                                                specialtiesList.forEach { specialty ->
                                                                    Row(
                                                                        verticalAlignment = Alignment.CenterVertically,
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .padding(
                                                                                horizontal = 16.dp,
                                                                                vertical = 8.dp
                                                                            )
                                                                            .clickable {
                                                                                viewModel.toggleSpecialtySelection(
                                                                                    "${specialty.code} ${specialty.name}"
                                                                                )
                                                                            }
                                                                    ) {
                                                                        Checkbox(
                                                                            checked = "${specialty.code} ${specialty.name}" in selectedSpecialties,
                                                                            onCheckedChange = {
                                                                                viewModel.toggleSpecialtySelection(
                                                                                    "${specialty.code} ${specialty.name}"
                                                                                )
                                                                            }
                                                                        )
                                                                        Spacer(
                                                                            modifier = Modifier.width(
                                                                                8.dp
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = "${specialty.code} ${specialty.name}",
                                                                            color = White
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
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showSpecialtiesDialog = false; viewModel.setSpec() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Готово")
                            }
                        }
                    )
                }
            }

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

@Composable
fun ProfileInfoItem(value: Set<Subject>, state: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Предмет",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                if (state == 4) {
                    Text(
                        text = "Балл",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            value.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.subject,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (item.ball != null) {
                        Text(
                            text = item.ball.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun ProfileSpecialityItem(value: Set<String>) {
    val maxVisibleItems = 4
    val remainingCount = value.size - maxVisibleItems

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            value.take(maxVisibleItems).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (remainingCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "и ещё $remainingCount",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
