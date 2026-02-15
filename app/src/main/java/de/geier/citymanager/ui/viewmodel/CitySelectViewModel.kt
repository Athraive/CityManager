package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.data.repository.CityRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CitySelectViewModel(
    private val repository: CityRepository
) : ViewModel() {

    val cities: StateFlow<List<CityEntity>> =
        repository.cities.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun createCity(
        name: String,
        gameMasterCode: String,
        themePreset: String,
        backgroundMode: String,
        backgroundValue: String?,
        onCreated: (String) -> Unit
    ) {
        val newId = UUID.randomUUID().toString()

        val city = CityEntity(
            id = newId,
            name = name,
            gameMasterCode = gameMasterCode,
            createdAt = System.currentTimeMillis(),
            themePreset = themePreset,
            backgroundMode = backgroundMode,
            backgroundValue = backgroundValue
        )

        viewModelScope.launch {
            repository.createCity(city)
            onCreated(newId)
        }
    }

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            repository.deleteCity(cityId)
        }
    }
}
