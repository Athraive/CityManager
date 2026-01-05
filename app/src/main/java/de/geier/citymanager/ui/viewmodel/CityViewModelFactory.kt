package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository

class CityViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            val db = DatabaseProvider.getDatabase(context)
            val categoryRepo = PoiCategoryRepository(db.poiCategoryDao())
            val poiRepo = PointOfInterestRepository(db.pointOfInterestDao())
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(categoryRepo, poiRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
