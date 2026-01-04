package de.geier.citymanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.PoiCategory
import de.geier.citymanager.ui.PointOfInterest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val database =
        DatabaseProvider.getDatabase(application)

    private val categoryRepository =
        PoiCategoryRepository(database.poiCategoryDao())

    private val poiRepository =
        PointOfInterestRepository(database.pointOfInterestDao())

    // ─────────────────────────────
    // Kategorien-State
    // ─────────────────────────────
    private val _categories =
        MutableStateFlow<List<PoiCategory>>(emptyList())

    val categories: StateFlow<List<PoiCategory>> = _categories

    // ─────────────────────────────
    // POIs-State
    // ─────────────────────────────
    private val _pois =
        MutableStateFlow<List<PointOfInterest>>(emptyList())

    val pois: StateFlow<List<PointOfInterest>> = _pois

    // ─────────────────────────────
    // Aktionen
    // ─────────────────────────────
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAll()
        }
    }

    fun saveCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.save(category)
            loadCategories()
        }
    }

    fun deleteCategory(category: PoiCategory) {
        viewModelScope.launch {
            categoryRepository.delete(category)
            loadCategories()
        }
    }

    fun loadPoisForCategory(categoryId: String) {
        viewModelScope.launch {
            _pois.value = poiRepository.getByCategory(categoryId)
        }
    }

    fun savePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.save(poi)
            loadPoisForCategory(poi.categoryId)
        }
    }

    fun deletePoi(poi: PointOfInterest) {
        viewModelScope.launch {
            poiRepository.delete(poi)
            loadPoisForCategory(poi.categoryId)
        }
    }
}
