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
        backgroundPreset: String,
        fontPreset: String,
        stylePreset: String,
        onCreated: (String) -> Unit
    ) {
        val newId = UUID.randomUUID().toString()

        val city = CityEntity(
            id = newId,
            name = name,
            gameMasterCode = gameMasterCode,
            createdAt = System.currentTimeMillis(),

            // Alt (noch vorhanden, aber später entfernbar)
            backgroundPreset = backgroundPreset,
            backgroundImageUri = null,

            // Aktiv genutzt
            fontPreset = fontPreset,
            stylePreset = stylePreset
        )

        viewModelScope.launch {
            repository.createCity(city)
            onCreated(newId)
        }
    }

    fun saveCity(city: CityEntity) {

        viewModelScope.launch {

            repository.save(city)
        }
    }

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            repository.deleteCity(cityId)
        }
    }
}