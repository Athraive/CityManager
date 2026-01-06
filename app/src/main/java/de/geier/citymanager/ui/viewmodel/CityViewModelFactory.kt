package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.FactionRepository

class CityViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {

            val db = DatabaseProvider.getDatabase(context)

            val categoryRepo = PoiCategoryRepository(db.poiCategoryDao())
            val poiRepo = PointOfInterestRepository(db.pointOfInterestDao())

            // In-Memory Repository für Fraktionen
            val factionRepo = FactionRepository()

            @Suppress("UNCHECKED_CAST")
            return CityViewModel(
                categoryRepo,
                poiRepo,
                factionRepo
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
