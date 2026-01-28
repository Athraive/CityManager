package de.geier.citymanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PoiCategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PoiCategoryViewModel(
    private val accessContext: AccessContext,
    private val repository: PoiCategoryRepository
) : ViewModel() {

    val categories = repository.categories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun save(category: PoiCategory) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            repository.save(category)
        }
    }

    fun delete(category: PoiCategory) {
        if (!accessContext.canEdit()) return

        viewModelScope.launch {
            repository.delete(category)
        }
    }
}
