package com.example.myspec.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myspec.R
import com.example.myspec.components.LoadingAnimation
import com.example.myspec.components.controls.buttons.GradientButton
import com.example.myspec.components.Logo
import com.example.myspec.components.controls.TriStateCheckbox
import com.example.myspec.components.text.ThinText
import com.example.myspec.constants.FirstSettingSteps
import com.example.myspec.constants.Other.Companion.egeSubjects
import com.example.myspec.models.Subject
import com.example.myspec.models.toCodeNameStrings
import com.example.myspec.ui.theme.Orange
import com.example.myspec.ui.theme.White
import com.example.myspec.viewmodels.auth.FirstSettingViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FirstSettingScreen(navController: NavController, viewModel: FirstSettingViewModel = viewModel()) {

    val step by viewModel.step.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val specialties by viewModel.specialties.collectAsState()
    val selectedSpecialties by viewModel.selectedSpecialties.collectAsState()
    val expandedGroups = viewModel.expandedGroups
    val expandedDirections = viewModel.expandedDirections
    val selectedSubjects by viewModel.selectedSubjects.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Logo()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                }
            ) { currentStep ->
                when (currentStep) {
                    FirstSettingSteps.WELCOME -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ThinText(stringId = R.string.welcome, 34)
                            ThinText(stringId = R.string.first_setting_start_ask)
                            Spacer(modifier = Modifier.height(10.dp))
                            GradientButton(
                                stringId = R.string.first_setting_start_button,
                                onClick = viewModel::startFirstSetting
                            )
                            GradientButton(stringId = R.string.authorization_logout) {
                                viewModel.logout()
                                navController.navigate("auth")
                            }
                        }
                    }

                    FirstSettingSteps.FOOTING_ASK -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ThinText(stringId = R.string.footing_ask, 34)
                            Spacer(modifier = Modifier.height(10.dp))
                            GradientButton(stringId = R.string.first_setting_footing_ege) {
                                viewModel.moveToStep(FirstSettingSteps.SELECT_SUBJECT_ASK)
                            }
                            GradientButton(stringId = R.string.first_setting_footing_spo) {
                                viewModel.setSPO()
                            }
                        }
                    }

                    FirstSettingSteps.SELECT_SUBJECT_ASK -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ThinText(stringId = R.string.first_setting_subject_ask)
                            Spacer(modifier = Modifier.height(10.dp))
                            GradientButton(stringId = R.string.button_yes) {
                                viewModel.moveToStep(FirstSettingSteps.SELECT_SUBJECT_DO)
                            }
                            GradientButton(stringId = R.string.button_no) {
                                viewModel.moveToStep(FirstSettingSteps.SELECT_SPEC)
                            }
                        }
                    }

                    FirstSettingSteps.SELECT_SUBJECT_DO -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ThinText(R.string.first_setting_selection_subjects)
                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 0.dp,
                                                bottomEnd = 0.dp
                                            )
                                        )
                                        .background(
                                            brush = Brush.verticalGradient(
                                                0.05f to Color.Black.copy(alpha = 0.1f),
                                                0.95f to Orange.copy(alpha = 0.1f),
                                                1f to Color.Transparent
                                            )
                                        )
                                ) {
                                    egeSubjects.forEach { subject ->
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.toggleSubjectSelection(Subject(subject),
                                                        !selectedSubjects.any {it.subject == subject})
                                                }
                                                .padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = selectedSubjects.any { it.subject == subject },
                                                onCheckedChange = { isChecked ->
                                                    viewModel.toggleSubjectSelection(Subject(subject), isChecked)
                                                }
                                            )
                                            Text(subject, modifier = Modifier.padding(start = 8.dp), color = White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            GradientButton(
                                stringId = R.string.button_continue,
                                onClick = viewModel::setSubjects,
                                enabled = selectedSubjects.isNotEmpty()
                            )
                        }
                    }

                    FirstSettingSteps.SELECT_SPEC -> {
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
                                        val groupSpecialties = directionsMap.flatMap { it.value }
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
                                                        selectedSpecialties.containsAll(specialtiesList.toCodeNameStrings()) -> true
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


                            Spacer(modifier = Modifier.height(16.dp))

                            GradientButton(
                                stringId = R.string.button_continue,
                                onClick = viewModel::setSpec
                            )
                        }
                    }
                    FirstSettingSteps.FINAL -> {navController.navigate("list")}
                }
            }

            if (loading) {
                LoadingAnimation()
            }
        }
    }
}


