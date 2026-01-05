package de.geier.citymanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CityViewModel(
    private val categoryRepository: PoiCategoryRepository,
    private val poiRepository: PointOfInterestRepository
) : ViewModel() {

    val categories = categoryRepository.categories
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun saveCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.save(category)
        }
    }

    fun deleteCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.delete(category)
        }
    }

    fun poisByCategory(categoryId: String) =
        poiRepository.getByCategory(categoryId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun savePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.save(poi)
        }
    }

    fun deletePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.delete(poi)
        }
    }
}
