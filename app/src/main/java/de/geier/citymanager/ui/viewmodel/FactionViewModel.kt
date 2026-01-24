package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.FactionRepositoryImpl
import de.geier.citymanager.ui.Faction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FactionViewModel(
    private val repository: FactionRepositoryImpl
) : ViewModel() {

    val factions: StateFlow<List<Faction>> =
        repository.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun save(faction: Faction) {
        viewModelScope.launch {
            repository.save(faction)
        }
    }

    fun delete(faction: Faction) {
        viewModelScope.launch {
            repository.delete(faction)
        }
    }
}
