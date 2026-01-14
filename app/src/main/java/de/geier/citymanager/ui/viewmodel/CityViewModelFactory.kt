package de.geier.citymanager.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.CityDistrictRepository
import de.geier.citymanager.data.repository.CityDistrictRepositoryImpl
import de.geier.citymanager.data.repository.PoiCategoryRepository
import de.geier.citymanager.data.repository.PointOfInterestRepository
import de.geier.citymanager.ui.FactionRepository

class CityViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {

            // ✅ korrekt für dein Projekt
            val database = DatabaseProvider.getDatabase(context)

            // ---------------- Repositories ----------------

            val categoryRepository =
                PoiCategoryRepository(database.poiCategoryDao())

            val poiRepository =
                PointOfInterestRepository(database.pointOfInterestDao())

            val factionRepository =
                FactionRepository() // ✅ parameterlos

            val cityDistrictRepository: CityDistrictRepository =
                CityDistrictRepositoryImpl(database.cityDistrictDao())

            return CityViewModel(
                categoryRepository = categoryRepository,
                poiRepository = poiRepository,
                factionRepository = factionRepository,
                cityDistrictRepository = cityDistrictRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
