package com.example.myspec.viewmodels.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myspec.models.RefreshToken
import com.example.myspec.api.RetrofitInstance
import com.example.myspec.api.body.SaveBonusBody
import com.example.myspec.api.body.SpecialityBody
import com.example.myspec.api.body.SubjectBody
import com.example.myspec.api.response.SpecialityGetResponse
import com.example.myspec.keystore.TokenManager.getRefreshToken
import com.example.myspec.keystore.TokenManager.removeRefreshToken
import com.example.myspec.keystore.TokenManager.saveRefreshToken
import com.example.myspec.models.Bonus
import com.example.myspec.models.Subject
import com.example.myspec.models.toCodeNameStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state

    private val _exit = MutableStateFlow(false)
    val exit: StateFlow<Boolean> = _exit

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _specialties = MutableStateFlow<SpecialityGetResponse?>(null)
    val specialties: StateFlow<SpecialityGetResponse?> = _specialties

    private val _selectedSpecialties = MutableStateFlow<Set<String>>(emptySet())
    val selectedSpecialties: StateFlow<Set<String>> = _selectedSpecialties

    private val _selectedSubjects = MutableStateFlow(setOf<Subject>())
    val selectedSubjects: StateFlow<Set<Subject>> = _selectedSubjects.asStateFlow()

    private val _expandedGroups = mutableStateSetOf<String>()
    val expandedGroups: SnapshotStateSet<String> = _expandedGroups

    private val _expandedDirections = mutableStateSetOf<String>()
    val expandedDirections: SnapshotStateSet<String> = _expandedDirections

    private val _bonusList = MutableStateFlow(Bonus())
    var bonusList: StateFlow<Bonus> = _bonusList

    fun logout() {
        val context = getApplication<Application>().applicationContext
        removeRefreshToken(context)
        _exit.value = true
    }

    fun toggleBonus(bonusNumber: Int) {
        _bonusList.update { current ->
            val newValue = when (bonusNumber) {
                1 -> current.copy(isGTO = !current.isGTO)
                2 -> current.copy(isPefectAttestat = !current.isPefectAttestat)
                3 -> current.copy(isPerfectSPO = !current.isPerfectSPO)
                4 -> current.copy(isPortfolio = !current.isPortfolio)
                5 -> current.copy(isVolunteer = !current.isVolunteer)
                6 -> current.copy(isEssay = !current.isEssay)
                else -> {
                    current
                }
            }
            newValue
        }
    }

    fun saveSubjectsUpdate() {
        viewModelScope.launch {
            try {
                _specialties.value = RetrofitInstance.api.getSpeciality()
                val context = getApplication<Application>().applicationContext
                val token = getRefreshToken(context)
                if (token != null && token != "") {
                    val subjectResponse = RetrofitInstance.api.setSubject(
                        SubjectBody(
                            token = RefreshToken(token),
                            subjects = _selectedSubjects.value
                        )
                    )
                    if (subjectResponse.refreshToken != "") {
                        saveRefreshToken(context, subjectResponse.refreshToken)
                    }

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
                Log.e("setSubjects", e.toString())
            } finally {
            }
        }

    }

    fun toggleSubject(subject: Subject, isChecked: Boolean? = null) {
        if (isChecked != null) {
            _selectedSubjects.value = if (isChecked) {
                _selectedSubjects.value + subject
            } else {
                _selectedSubjects.value - subject
            }
        } else {
            _selectedSubjects.value = if (subject in _selectedSubjects.value) {
                _selectedSubjects.value - subject
            } else {
                _selectedSubjects.value + subject
            }
        }


    }

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

    fun setSpec() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = getRefreshToken(context)
                if (token != null && token != "") {
                    val specialityResponse = RetrofitInstance.api.setSpeciality(
                        SpecialityBody(
                            token = RefreshToken(token),
                            specialties = _selectedSpecialties.value.toList()
                        )
                    )
                    if (specialityResponse.refreshToken != "") {
                        saveRefreshToken(context, specialityResponse.refreshToken)
                    }
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
            }
        }
    }

    fun loadingProfile() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val context = getApplication<Application>().applicationContext
                val token = getRefreshToken(context)
                if (token != null && token != "") {
                    val response = RetrofitInstance.api.getProfile(RefreshToken(token))
                    if (response.refreshToken != "") {
                        saveRefreshToken(context, response.refreshToken)
                    }
                    _email.value = response.email
                    _state.value = response.state
                    _selectedSubjects.value = response.subjects
                    _selectedSpecialties.value = response.specialities
                    _bonusList.value = response.bonus
                }
                _specialties.value = RetrofitInstance.api.getSpeciality()
            } catch (e: HttpException) {
                _error.value = "Произошла ошибка ${e.code()}"
            } catch (e: Exception) {
                _error.value = "Похоже что-то сломалось"
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPassword(){
        Toast.makeText(getApplication(),
            "Эта функция отключена!", Toast.LENGTH_SHORT).show()
    }

    fun saveBonus() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = getRefreshToken(context)
                if (token != null && token != "") {
                    val response = RetrofitInstance.api.saveBonus(
                        SaveBonusBody(
                            RefreshToken(token),
                            _bonusList.value
                        )
                    )
                    if (response.refreshToken != "") {
                        saveRefreshToken(context, response.refreshToken)
                    }
                    Toast.makeText(context, "Данные успешно сохранены :)", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: HttpException) {
               Log.e("SaveBonusHttp",e.message())
            } catch (e: Exception) {
                Log.e("SaveBonus", e.toString())
            } finally {
            }
        }
    }
}