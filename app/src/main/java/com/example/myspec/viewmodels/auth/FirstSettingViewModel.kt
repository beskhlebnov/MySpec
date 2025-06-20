package com.example.myspec.viewmodels.auth

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myspec.models.RefreshToken
import com.example.myspec.api.RetrofitInstance
import com.example.myspec.api.body.SubjectBody
import com.example.myspec.api.body.SpecialityBody
import com.example.myspec.api.response.SpecialityGetResponse
import com.example.myspec.constants.FirstSettingSteps
import com.example.myspec.keystore.TokenManager
import com.example.myspec.models.Subject
import com.example.myspec.models.toCodeNameStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FirstSettingViewModel(application: Application) : AndroidViewModel(application) {

    private val _specialties = MutableStateFlow<SpecialityGetResponse?>(null)
    val specialties: StateFlow<SpecialityGetResponse?> = _specialties

    private val _selectedSpecialties = MutableStateFlow<Set<String>>(mutableStateSetOf())
    val selectedSpecialties: StateFlow<Set<String>> = _selectedSpecialties

    private val _expandedGroups = mutableStateSetOf<String>()
    val expandedGroups: SnapshotStateSet<String> = _expandedGroups

    private val _expandedDirections = mutableStateSetOf<String>()
    val expandedDirections: SnapshotStateSet<String> = _expandedDirections

    private val _step = MutableStateFlow(FirstSettingSteps.WELCOME)
    val step: StateFlow<FirstSettingSteps> = _step

    private val _is_spo = MutableStateFlow(true)
    val is_spo: StateFlow<Boolean> = _is_spo

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _selectedSubjects = MutableStateFlow(setOf(Subject("Русский язык"), Subject("Математика (база)")))
    val selectedSubjects: StateFlow<Set<Subject>> = _selectedSubjects.asStateFlow()

    fun toggleGroupSelection(group: String) {
        val specialtiesData = _specialties.value ?: return
        val groupSpecialties = specialtiesData[group]?.flatMap { it.value } ?: return

        _selectedSpecialties.update { currentSelection ->
            if (currentSelection.containsAll(groupSpecialties.toCodeNameStrings())) {
                currentSelection - groupSpecialties.toCodeNameStrings().toSet()
            } else {
                currentSelection + groupSpecialties.toCodeNameStrings()
            }
        }
    }

    fun toggleDirectionSelection(group: String, direction: String) {
        val specialtiesData = _specialties.value ?: return
        val directionSpecialties = specialtiesData[group]?.get(direction) ?: return

        _selectedSpecialties.update { currentSelection ->
            if (currentSelection.containsAll(directionSpecialties.toCodeNameStrings())) {
                currentSelection - directionSpecialties.toCodeNameStrings()
            } else {
                currentSelection + directionSpecialties.toCodeNameStrings()
            }
        }
    }

    fun toggleSpecialtySelection(specialty: String) {
        _selectedSpecialties.update { current ->
            if (current.contains(specialty)) {
                current - specialty
            } else {
                current + specialty
            }
        }
    }

    fun toggleGroupExpansion(group: String) {
        if (expandedGroups.contains(group)) {
            expandedGroups.remove(group)
        } else {
            expandedGroups.add(group)
        }
    }

    fun toggleDirectionExpansion(direction: String) {
        if (expandedDirections.contains(direction)) {
            expandedDirections.remove(direction)
        } else {
            expandedDirections.add(direction)
        }
    }

    fun toggleSubjectSelection(subject: Subject, isChecked: Boolean) {
        if (isChecked) { addSubject(subject) }
        else { removeSubject(subject) }
        Log.v("SET", _selectedSubjects.value.toString())
    }

    private fun removeSubject(subject: Subject){
        _selectedSubjects.value = if (subject.subject == "Математика (профиль)")
        {
            _selectedSubjects.value - subject + Subject("Математика (база)")
        }
        else{
            _selectedSubjects.value - subject
        }
    }

    private fun addSubject(subject: Subject){
        _selectedSubjects.value = if (subject.subject == "Математика (профиль)")
        {
            _selectedSubjects.value + subject - Subject("Математика (база)")
        }
        else{
            _selectedSubjects.value + subject
        }
    }

    fun moveToStep(step: FirstSettingSteps) {
        _step.value = step
    }

    fun setSPO() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = TokenManager.getRefreshToken(context)
                if (token != null && token != "") {
                    val refreshToken = RetrofitInstance.api.setSpo(RefreshToken(token))
                    if (refreshToken.refreshToken != "") {
                        TokenManager.saveRefreshToken(context, refreshToken.refreshToken)
                    }
                    moveToStep(FirstSettingSteps.SELECT_SPEC)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        Log.e(e.code().toString(), e.message())
                    }

                    401 -> {
                        Log.e(e.code().toString(), e.message())
                    }
                }
            } catch (e: Exception) {
                Log.e("setSpec", e.toString())
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        val context = getApplication<Application>().applicationContext
        TokenManager.saveRefreshToken(context, "")
    }

    fun startFirstSetting() {
        loadSpecialties()
        moveToStep(FirstSettingSteps.FOOTING_ASK)
    }

    private fun loadSpecialties() {
        viewModelScope.launch {
            try {
                _specialties.value = RetrofitInstance.api.getSpeciality()
            } catch (e: Exception) {
                Log.e("error_sp", e.toString())
            } finally {
            }
        }
    }

    fun setSpec(){
        _loading.value = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = TokenManager.getRefreshToken(context)
                if (token != null && token != "") {
                    val specialityResponse = RetrofitInstance.api.setSpeciality(
                        SpecialityBody(
                            token = RefreshToken(token),
                            specialties = _selectedSpecialties.value.toList()
                        )
                    )
                    if (specialityResponse.refreshToken != "") {
                        TokenManager.saveRefreshToken(context, specialityResponse.refreshToken)
                    }
                    _step.value = FirstSettingSteps.FINAL
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        Log.e(e.code().toString(), e.message())
                    }

                    401 -> {
                        Log.e(e.code().toString(), e.message())
                    }
                }
            } catch (e: Exception) {
                Log.e("setSpec", e.toString())
            } finally {
                _loading.value = false
            }
        }
    }

    fun setSubjects() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = TokenManager.getRefreshToken(context)
                if (token != null && token != "") {
                    val subjectResponse = RetrofitInstance.api.setSubject(
                        SubjectBody(
                            token = RefreshToken(token),
                            subjects = _selectedSubjects.value
                        )
                    )
                    if (subjectResponse.refreshToken != "") {
                        TokenManager.saveRefreshToken(context, subjectResponse.refreshToken)
                    }
                }
                _step.value = FirstSettingSteps.SELECT_SPEC
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        Log.e(e.code().toString(), e.message())
                    }

                    401 -> {
                        Log.e(e.code().toString(), e.message())
                    }
                }
            } catch (e: Exception) {
                Log.e("setSubjects", e.toString())
            } finally {
                _loading.value = false
            }
        }
    }


}